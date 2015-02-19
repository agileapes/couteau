/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
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

package com.agileapes.couteau.concurrency.manager;

import com.agileapes.couteau.concurrency.deferred.Promise;
import com.agileapes.couteau.concurrency.error.TaskContextException;
import com.agileapes.couteau.concurrency.task.DeferredCallable;
import com.agileapes.couteau.concurrency.task.Task;

/**
 * This interface encapsulates all that a task manager needs to perform. This includes
 * operations that will facilitate scheduling tasks and canceling them.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/15, 5:54)
 */
public interface TaskManager extends Runnable {

    /**
     * Schedules the given task to be executed
     * @param task    the task that is to be executed
     */
    void schedule(Task task);

    /**
     * Defers the execution of a task which will eventually yield a specific and expected result
     * @param task    the task to be performed
     * @param <E>     the type of the result which will be yielded by the execution of the task
     * @return the promise of the completion of the task
     */
    <E> Promise<E> defer(DeferredCallable<E> task);

    /**
     * Defers the execution of the task until the given promise has been resolved. This promise will not be
     * resolved unless the given promise has been resolved externally, and the scheduled task's turn has been
     * executed.
     * @param task    the task to be executed
     * @param <E>     the type of the result for the promise
     * @return the promise for the task
     */
    <E> Promise<E> defer(Promise<E> task);

    /**
     * Marks the given task as done within this task manager
     * @param task    the task that was completed
     * @throws TaskContextException if the task has not been scheduled with this task manager
     */
    void done(Task task) throws TaskContextException;

    /**
     * Signals a failure in carrying out the task
     * @param task         the task that has failed
     * @param exception    the error leading to this failure
     * @throws TaskContextException if the task has not been scheduled with this task manager
     */
    void fail(Task task, Exception exception) throws TaskContextException;

    /**
     * Shuts down the task manager gracefully
     */
    void shutdown();

    /**
     * @return a descriptor for the task manager so that its execution flow can be monitored more closely
     */
    TaskManagerStatus getStatus();

    /**
     * @return the task manager's given name
     */
    String getName();

}
