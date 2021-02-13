package com.jfxbase.impl.tasks;

import com.jfxbase.base.tasks.IPermanentObject;
import com.jfxbase.base.tasks.ITaskManager;
import com.jfxbase.base.tasks.ITaskWrapper;

import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @param <K> key type
 * @author sshahine
 */
public class TaskManager<K> implements ITaskManager<K> {
    // execution pool (e.g can be sync with ui)
    private final ExecutorService executionPool;
    private final ExecutorCompletionService completionService;
    // hidden execution tool (e.g runs in background, ui is not affected)
    private final ExecutorService hiddenExecutionPool;
    private final ExecutorCompletionService hiddenCompletionService;
    // thread pool to monitor execution pool state
    private final ExecutorService completionPool;
    private final ConcurrentHashMap<K, ConcurrentLinkedQueue<Future<?>>> tasksKeyMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<K, ITaskWrapper> tasksWrappersMap = new ConcurrentHashMap<>();

    private Consumer<Throwable> uncaughtThrowableConsumer = throwable -> throwable.printStackTrace();

    public TaskManager(int threads) {
        this.executionPool = createCustomPool(threads, new ExecutionThreadFactory());
        this.completionService = new ExecutorCompletionService(executionPool);
        this.hiddenExecutionPool = createCustomPool(threads, new HiddenExecutionThreadFactory());
        this.hiddenCompletionService = new ExecutorCompletionService(hiddenExecutionPool);
        // thread pool to monitor execution pool state
        this.completionPool = Executors.newFixedThreadPool(threads, runnable -> {
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setName("Awaiting Tasks Completion Thread...");
            return thread;
        });
    }

    private ThreadPoolExecutor createCustomPool(int threads, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(threads, threads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), threadFactory) {

            @Override
            protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
                return new HandledTask<T>(runnable, value);
            }

            @Override
            protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
                return new HandledTask<T>(callable);
            }

            class HandledTask<T> extends FutureTask<T> {
                private HandledTask(Callable<T> callable) {
                    super(callable);
                }

                private HandledTask(Runnable runnable, T value) {
                    super(runnable, value);
                }

                @Override
                protected void done() {
                    try {
                        if (!isCancelled()) {
                            get();
                        }
                    } catch (ExecutionException e) {
                        // Exception occurred, deal with it
                        if (uncaughtThrowableConsumer != null) {
                            uncaughtThrowableConsumer.accept(e.getCause());
                        }
                    } catch (InterruptedException e) {
                        // Shouldn't happen, we're invoked when computation is finished
                        throw new AssertionError(e);
                    }
                }
            }
        };
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        shutdown();
    }

    @Override
    public void shutdown() {
        executionPool.shutdown();
        completionPool.shutdown();
        hiddenExecutionPool.shutdown();
    }

    @Override
    public ITaskWrapper addTaskWrapper(K key, ITaskWrapper wrapper) {
        if (tasksWrappersMap.get(key) instanceof IPermanentObject) {
            throw new RuntimeException("Can't replace permanent task wrapper for tasks key : " + key);
        }
        return tasksWrappersMap.put(key, wrapper);
    }

    @Override
    public ITaskWrapper removeTaskWrapper(K key) {
        return tasksWrappersMap.remove(key);
    }

    @Override
    public synchronized void submitTask(Runnable task, K key) {
        postExecute(key, completionService.submit(task, null));
    }

    @Override
    public synchronized <V> Future<V> submitTask(Callable<V> task, K key) {
        Future<V> res = completionService.submit(task);
        postExecute(key, res);
        return res;
    }

    @Override
    public void submitHiddenTask(Runnable task) {
        hiddenCompletionService.submit(task, null);
    }

    @Override
    public <V> Future<V> submitHiddenTask(Callable<V> task) {
        return hiddenCompletionService.submit(task);
    }

    private void postExecute(K key, Future future) {
        synchronized (key) {
            tasksKeyMap.putIfAbsent(key, new ConcurrentLinkedQueue());
            tasksKeyMap.get(key).offer(future);
            if (tasksKeyMap.get(key).size() == 1) {
                completionPool.submit(() -> completionTask.accept(key));
                ITaskWrapper wrapper = tasksWrappersMap.get(key);
                if (wrapper != null) {
                    wrapper.started();
                }
            }
        }
    }

    private Consumer<K> completionTask = key -> {
        while (!tasksKeyMap.get(key).isEmpty()) {
            try {
                tasksKeyMap.get(key).peek().get();
            } catch (Exception e) {
            }
            tasksKeyMap.get(key).poll();
        }
        synchronized (key) {
            tasksKeyMap.remove(key);
            ITaskWrapper wrapper = tasksWrappersMap.get(key);
            if (wrapper != null) {
                wrapper.finished();
            }
            if (!(wrapper instanceof IPermanentObject)) {
                removeTaskWrapper(key);
            }
        }
    };


    // pool for visible tasks
    static class ExecutionThreadFactory implements ThreadFactory {
        private static final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

        @Override
        public Thread newThread(Runnable run) {
            Thread thread = defaultFactory.newThread(run);
            thread.setName("Execution thread : " + thread.getName());
            return thread;
        }
    }

    // pool for hidden tasks
    static class HiddenExecutionThreadFactory implements ThreadFactory {
        private static final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

        @Override
        public Thread newThread(Runnable run) {
            Thread thread = defaultFactory.newThread(run);
            thread.setName("Hidden execution thread : " + thread.getName());
            return thread;
        }
    }

    @Override
    public void setUncaughtThrowableConsumer(Consumer<Throwable> uncaughtThrowableConsumer) {
        this.uncaughtThrowableConsumer = uncaughtThrowableConsumer;
    }
}
