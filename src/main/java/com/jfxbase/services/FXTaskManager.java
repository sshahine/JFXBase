package com.jfxbase.services;

import com.jfxbase.base.tasks.ITaskManager;
import com.jfxbase.base.tasks.ITaskWrapper;
import com.jfxbase.impl.tasks.TaskManager;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;


public final class FXTaskManager implements ITaskManager<String> {

    private final TaskManager<String> taskManager = new TaskManager<>(5);

    /****************************************************************
     *
     * Thread safe singleton
     *
     ***************************************************************/

    private static volatile FXTaskManager instance;

    private FXTaskManager() {
        // Protect against instantiation via reflection
        if (instance == null) {
            instance = this;
        } else {
            throw new IllegalStateException("Already initialized.");
        }
    }

    public static synchronized FXTaskManager getInstance() {
        if (instance == null) {
            synchronized (FXTaskManager.class) {
                if (instance == null) {
                    instance = new FXTaskManager();
                }
            }
        }
        return instance;
    }

    /****************************************************************
     *
     * Methods
     *
     ***************************************************************/

    @Override
    public void submitTask(Runnable task, String key) {
        taskManager.submitTask(task, key);
    }

    @Override
    public <V> Future<V> submitTask(Callable<V> task, String key) {
        return taskManager.submitTask(task, key);
    }

    @Override
    public void submitHiddenTask(Runnable task) {
        taskManager.submitHiddenTask(task);
    }

    @Override
    public <V> Future<V> submitHiddenTask(Callable<V> task) {
        return taskManager.submitHiddenTask(task);
    }

    @Override
    public ITaskWrapper addTaskWrapper(String key, ITaskWrapper wrapper) {
        return taskManager.addTaskWrapper(key, wrapper);
    }

    @Override
    public ITaskWrapper removeTaskWrapper(String key) {
        return taskManager.removeTaskWrapper(key);
    }

    @Override
    public void shutdown() {
        taskManager.shutdown();
    }

    @Override
    public void setUncaughtThrowableConsumer(Consumer<Throwable> uncaughtThrowableConsumer) {
        taskManager.setUncaughtThrowableConsumer(uncaughtThrowableConsumer);
    }
}
