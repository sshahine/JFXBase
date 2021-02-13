package com.jfxbase.base.tasks;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public interface ITaskManager<K> {

    void submitTask(Runnable task, K key);

    <V> Future<V> submitTask(Callable<V> task, K key);

    void submitHiddenTask(Runnable task);

    <V> Future<V> submitHiddenTask(Callable<V> task);

    ITaskWrapper addTaskWrapper(K key, ITaskWrapper wrapper);

    ITaskWrapper removeTaskWrapper(K key);

    void shutdown();

    void setUncaughtThrowableConsumer(Consumer<Throwable> uncaughtThrowableConsumer);
}
