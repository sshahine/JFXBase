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
