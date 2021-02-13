package com.jfxbase.services;

import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.utils.JFXNodeUtils;
import com.jfoenix.utils.JFXUtilities;
import com.jfxbase.base.IFXNodeBuilder;
import com.jfxbase.base.IFXSwitcher;
import com.jfxbase.impl.animation.AnimationFactory;
import com.jfxbase.impl.builders.EmptyBuilder;
import com.jfxbase.impl.exceptions.BuildException;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author sshahine
 */
public class FXSwitcher implements IFXSwitcher {

    private final FXTaskManager taskManager = FXTaskManager.getInstance();

    // map for sub content switching
    private HashMap<Pane, SwitchContentProcess<SwitchArgs>> switchContentMap = new HashMap<>();

    private static volatile FXSwitcher instance;

    private FXSwitcher() {
        // Protect against instantiation via reflection
        if (instance == null) {
            instance = this;
        } else {
            throw new IllegalStateException("Already initialized.");
        }
    }

    public static synchronized FXSwitcher getInstance() {
        if (instance == null) {
            synchronized (FXSwitcher.class) {
                if (instance == null) {
                    instance = new FXSwitcher();
                }
            }
        }
        return instance;
    }

    /***************************************************************************
     *                                                                         *
     * API                                                                     *
     *                                                                         *
     **************************************************************************/

    @Override
    public void switchContent(Pane pane, IFXNodeBuilder page) {
        if (!switchContentMap.containsKey(pane)) {
            switchContentMap.put(pane, new SwitchContentProcess(pane));
        }
        switchContentMap.get(pane).switchContent(new SwitchArgs(page));
    }

    /***************************************************************************
     *                                                                         *
     *  Content Switch (It's a Base for Content switching)                     *
     *                                                                         *
     **************************************************************************/

    class SwitchArgs {
        IFXNodeBuilder builder;

        public SwitchArgs(IFXNodeBuilder builder) {
            this.builder = builder;
        }

        IFXNodeBuilder getBuilder() {
            return builder;
        }
    }

    class SwitchContentProcess<T extends SwitchArgs> {

        protected Pane parent;
        private Node currentContent;
        ReadOnlyObjectWrapper<IFXNodeBuilder> currentNodeBuilder = new ReadOnlyObjectWrapper<>(null);
        AtomicBoolean switching = new AtomicBoolean(false);
        private Deque<Runnable> eventsQueue = new ConcurrentLinkedDeque<>();
        private HashMap<IFXNodeBuilder, Runnable> runnablesMap = new HashMap<>();

        protected Consumer<T> initArgsConsumer = builder -> {
        };

        protected Consumer<T> endArgsConsumer = builder -> {
        };


        public SwitchContentProcess(Pane pane) {
            this.parent = pane;
            // remove context once the pane is no longer visible on scene
            pane.sceneProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    if (pane.getScene() == null) {
                        switchContentMap.remove(parent);
                        pane.sceneProperty().removeListener(this);
                    }
                }
            });
        }

        public void switchContent(T args) {
            if (switching.getAndSet(true)) {
                final Runnable oldRunnable = runnablesMap.get(args.getBuilder());
                if (oldRunnable != null) {
                    eventsQueue.offer(oldRunnable);
                } else {
                    final Runnable runnable = () -> switchContent(args);
                    runnablesMap.put(args.getBuilder(), runnable);
                    eventsQueue.add(runnable);
                }
                return;
            }
            taskManager.submitHiddenTask(() -> {
                try {
                    initArgsConsumer.accept(args);
                    IFXNodeBuilder builderArg = args.getBuilder();
                    final IFXNodeBuilder newBuilder = builderArg == null ? EmptyBuilder.getInstance() : builderArg;
                    final IFXNodeBuilder currentBuilder = this.currentNodeBuilder.get();
                    // exit current node
                    List<Animation> exitAnimations = new ArrayList<>(Arrays.asList(
                            showProgress(),
                            // TODO exitToolbar(currentBuilder),
                            currentBuilder == null ? null : currentBuilder.animateExit(currentContent)));
                    exitAnimations.removeAll(Collections.singleton(null));

                    ParallelTransition exitTransition = new ParallelTransition(exitAnimations.toArray(new Animation[0]));
                    exitTransition.setOnFinished(finished -> {
                        exitTransition.getChildren().clear();
                        this.currentNodeBuilder.set(newBuilder);
                        taskManager.submitHiddenTask(() -> {
                            // build the new content / load css
                            Node temp = null;
                            Animation entranceAnimation = null;
                            try {
                                temp = IFXNodeBuilder.build(newBuilder);
                                entranceAnimation = newBuilder.animateEntrance(temp);
                            } catch (BuildException e) {
                                e.printStackTrace();
                                temp = new StackPane(new Label(e.getMessage()));
                            }
                            Node newContent = temp;
                            currentContent = newContent;
                            endArgsConsumer.accept(args);

                            // load page animation after it's finished loading
                            // load toolbar items
                            List<Animation> enterAnimations = new ArrayList<>(Arrays.asList(
                                    //TODO: enterToolbar(newBuilder),

                                    // animate builder entrance
                                    entranceAnimation));
                            enterAnimations.removeAll(Collections.singleton(null));
                            ParallelTransition entranceTransition = new ParallelTransition(enterAnimations.toArray(new Animation[0]));
                            entranceTransition.setOnFinished(end -> {
                                if (!eventsQueue.isEmpty()) {
                                    Runnable task = eventsQueue.peekLast();
                                    eventsQueue.clear();
                                    switching.set(false);
                                    if (task != null) {
                                        taskManager.submitHiddenTask(task);
                                    }
                                } else {
                                    switching.set(false);
                                }
                            });
                            // animate after showing on the scene to fix animation glitches
                            JFXNodeUtils.addDelayedPropertyInvalidationListener(
                                    newContent.localToSceneTransformProperty(), Duration.millis(150), (transform, listener) -> {
                                        entranceTransition.playFromStart();
                                        hideProgress();
                                        newContent.localToSceneTransformProperty().removeListener(listener);
                                    }
                            );
                            // update content
                            JFXUtilities.runInFX(() -> {
                                if (progress != null) {
                                    parent.getChildren().setAll(newContent, progress);
                                } else {
                                    // this branch should not be covered in any case (never executed)
                                    System.err.println("Switch Content overlaps with tasks...");
                                    parent.getChildren().setAll(newContent);
                                }
                            });
                        });
                    });
                    exitTransition.playFromStart();
                } catch (Exception e) {
                    e.printStackTrace();
                    switching.set(false);
                }
            });
        }

        /***************************************************************************
         *                                                                         *
         * Progress Methods                                                        *
         *                                                                         *
         **************************************************************************/

        protected Supplier<Region> progressSupplier = () -> new JFXSpinner();
        protected Animation progressAnimation = null;
        protected Region progress = null;

        public Animation showProgress() {
            if (progressAnimation == null) {
                progress = progressSupplier.get();
                progress.setOpacity(0);
                JFXUtilities.runInFXAndWait(() -> {
                    addUnManagedNode(parent, progress);
                    if (progressAnimation != null) {
                        progressAnimation.stop();
                    }
                    progressAnimation = AnimationFactory.opacity(progress, 0, 1);
                });
            } else {
                progress.toFront();
            }
            return progressAnimation;
        }

        public void hideProgress() {
            if (progressAnimation != null) {
                progressAnimation.pause();
                progressAnimation.setRate(-1);
                final Region temp = progress;
                progressAnimation.setOnFinished((finish) -> {
                    parent.getChildren().remove(temp);
                });
                progressAnimation.play();
                progress = null;
                progressAnimation = null;
            }
        }
    }

    /***************************************************************************
     *                                                                         *
     * Add Un-Managed nodes to a pane (e.g progress), could be STATIC in Utils *
     *                                                                         *
     **************************************************************************/

    private void addUnManagedNode(Pane parent, Region unManagedChild) {
        unManagedChild.setManaged(false);
        parent.getChildren().add(unManagedChild);
        final double[] h = new double[]{-1};
        final double[] w = new double[]{-1};
        Runnable initSize = () -> {
            if (h[0] == -1 || w[0] == -1) {
                unManagedChild.applyCss();
                unManagedChild.autosize();
                h[0] = unManagedChild.getHeight();
                w[0] = unManagedChild.getWidth();
            }
        };
        if (parent.getScene() != null) {
            initSize.run();
            layout(parent, unManagedChild, h[0], w[0]);
        }

        final InvalidationListener parentLayoutListener = observable -> {
            initSize.run();
            layout(parent, unManagedChild, h[0], w[0]);
        };
        parent.widthProperty().addListener(parentLayoutListener);
        parent.heightProperty().addListener(parentLayoutListener);
        unManagedChild.parentProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (unManagedChild.getParent() != parent) {
                    parent.widthProperty().removeListener(parentLayoutListener);
                    parent.heightProperty().removeListener(parentLayoutListener);
                    unManagedChild.parentProperty().removeListener(this);
                }
            }
        });
    }

    private void layout(Region main, Region pr, double h, double w) {
        boolean resize = computeWidth(main, pr, w);
        resize = computeHeight(main, pr, h) || resize;
        if (resize) {
            pr.autosize();
        }
        pr.relocate((main.getWidth() / 2) - (pr.getWidth() / 2),
                (main.getHeight() / 2) - (pr.getHeight() / 2));
    }

    private boolean computeHeight(Region main, Region pr, double h) {
        boolean resize = false;
        double maxHeight = pr.getMaxHeight();
        final double parentHeight = main.getHeight();
        if (parentHeight < pr.getHeight()) {
            resize = true;
            pr.setMaxHeight(parentHeight);
        } else if (maxHeight != Region.USE_COMPUTED_SIZE) {
            resize = true;
            if (h > parentHeight) {
                pr.setMaxHeight(parentHeight);
            } else {
                pr.setMaxHeight(-1);
            }
        }
        return resize;
    }

    private boolean computeWidth(Region main, Region pr, double w) {
        boolean resize = false;
        double maxWidth = pr.getMaxWidth();
        final double parentWidth = main.getWidth();
        if (parentWidth < pr.getWidth()) {
            resize = true;
            pr.setMaxWidth(parentWidth);
        } else if (maxWidth != Region.USE_COMPUTED_SIZE) {
            resize = true;
            if (w > parentWidth) {
                pr.setMaxWidth(parentWidth);
            } else {
                pr.setMaxWidth(-1);
            }
        }
        return resize;
    }
}
