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
