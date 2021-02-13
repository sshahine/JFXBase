/**
 * Copyright (c) 2021 Shadi Shaheen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jfxbase.impl.todo;

public class ToolbarAnimation {
//    // TODO
//    /***************************************************************************
//     *                                                                         *
//     * STATIC METHODS : move outside the class                                 *
//     *                                                                         *
//     **************************************************************************/
//
//    protected Animation enterToolbar(IFXNodeBuilder page) {
//        page = CContextProxy.getActualObject(page);
//        if (page instanceof IFXToolBarBuilder) {
//            final Node toolbar = ((IFXToolBarBuilder) page).getToolbar();
//            if (toolbar == null) {
//                return null;
//            }
//            boolean isToolBarControl = toolbar instanceof ToolBar;
//            boolean isJFXToolbar = toolbar instanceof JFXToolbar;
//            final ObservableList<Node> rightItems = isToolBarControl ? ((ToolBar) toolbar).getItems()
//                    : (isJFXToolbar ? ((JFXToolbar) toolbar).getRightItems() : ((Pane) toolbar).getChildren());
//            Pane center = isJFXToolbar ? (Pane) ((JFXToolbar) toolbar).getCenter() : null;
//            center = center == null ? new StackPane() : center;
//            final ObservableList<Node> centerItems = center.getChildren();
//            Animation transition = getToolBarAnimation(rightItems, centerItems, 1);
//            if (transition != null) {
//                return transition;
//            }
//        }
//        return null;
//    }
//
//    protected Animation exitToolbar(IFXNodeBuilder page) {
//        page = CContextProxy.getActualObject(page);
//        if (page instanceof IFXToolBarBuilder) {
//            final Node toolbar = ((IFXToolBarBuilder) page).getToolbar();
//            if (toolbar == null) {
//                return null;
//            }
//            boolean isToolBarControl = toolbar instanceof ToolBar;
//            boolean isJFXToolbar = toolbar instanceof JFXToolbar;
//            final ObservableList<Node> rightItems = isToolBarControl ? ((ToolBar) toolbar).getItems()
//                    : (isJFXToolbar ? ((JFXToolbar) toolbar).getRightItems() : ((Pane) toolbar).getChildren());
//            Pane center = isJFXToolbar ? (Pane) ((JFXToolbar) toolbar).getCenter() : null;
//            center = center == null ? new StackPane() : center;
//            final ObservableList<Node> centerItems = center.getChildren();
//            Animation transition = getToolBarAnimation(rightItems, centerItems, 0);
//            if (transition != null) {
//                transition.setOnFinished(finish -> {
//                    rightItems.clear();
//                    centerItems.clear();
//                });
//                return transition;
//            }
//        }
//        return null;
//    }
//
//    protected final Animation getToolBarAnimation(ObservableList<Node> rightItems, ObservableList<Node> centerItems, double endValue) {
//        ParallelTransition transition = new ParallelTransition();
//        if (!rightItems.isEmpty()) {
//            transition.getChildren().addAll(animateItemsScale(rightItems, endValue));
//        }
//        if (!centerItems.isEmpty()) {
//            transition.getChildren().addAll(animateItemsScale(centerItems, endValue));
//        }
//        if (!transition.getChildren().isEmpty()) {
//            return transition;
//        }
//        return null;
//    }
//
//
//    protected final List<Animation> animateItemsScale(ObservableList<Node> items, double endValue) {
//        if (items.isEmpty()) {
//            return new ArrayList<>();
//        }
//        double totalDuration = 240; // millisec
//        int childCount = 0;
//        List<Animation> animations = new ArrayList<>();
//        for (int i = 0; i < items.size(); i++) {
//            Node item = items.get(i);
//            if (item instanceof HBox) {
//                childCount += ((HBox) item).getChildrenUnmodifiable().size();
//            } else {
//                childCount++;
//            }
//        }
//        Duration duration = Duration.millis(totalDuration / childCount);
//        Duration delay = duration.divide(2);
//        List<Node> temp = reverse(items, endValue);
//        for (int i = 0, deepIndex = 0; i < temp.size(); i++) {
//            Node item = temp.get(i);
//            if (item instanceof HBox) {
//                ObservableList<Node> subItems = ((HBox) item).getChildren();
//                List<Node> subTemp = reverse(subItems, endValue);
//                for (int j = 0; j < subTemp.size(); j++) {
//                    Node subItem = subTemp.get(j);
//                    int finalDeepIndex = deepIndex;
//                    animations.add(new CachedTransition(subItem, new Timeline(new KeyFrame(Duration.millis(1000),
//                            new KeyValue(subItem.scaleXProperty(), endValue, Interpolator.EASE_BOTH),
//                            new KeyValue(subItem.scaleYProperty(), endValue, Interpolator.EASE_BOTH),
//                            new KeyValue(subItem.opacityProperty(), endValue, Interpolator.EASE_BOTH)))) {{
//                        setCycleDuration(duration);
//                        setDelay(delay.multiply(finalDeepIndex));
//                    }});
//                    deepIndex++;
//                }
//            } else {
//                int finalDeepIndex = deepIndex;
//                animations.add(new CachedTransition(item, new Timeline(new KeyFrame(Duration.millis(1000),
//                        new KeyValue(item.scaleXProperty(), endValue, Interpolator.EASE_BOTH),
//                        new KeyValue(item.scaleYProperty(), endValue, Interpolator.EASE_BOTH),
//                        new KeyValue(item.opacityProperty(), endValue, Interpolator.EASE_BOTH)))) {{
//                    setCycleDuration(duration);
//                    setDelay(delay.multiply(finalDeepIndex));
//                }});
//                deepIndex++;
//            }
//        }
//        return animations;
//    }
//
//    private List<Node> reverse(ObservableList<Node> items, double endValue) {
//        List<Node> temp = null;
//        if (endValue == 1) {
//            temp = new ArrayList<>(items);
//            Collections.reverse(temp);
//        } else {
//            temp = items;
//        }
//        return temp;
//    }
}
