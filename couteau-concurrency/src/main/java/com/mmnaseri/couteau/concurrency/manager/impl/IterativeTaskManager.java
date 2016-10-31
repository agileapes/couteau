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

package com.mmnaseri.couteau.concurrency.manager.impl;

import com.mmnaseri.couteau.concurrency.deferred.Deferred;
import com.mmnaseri.couteau.concurrency.deferred.Promise;
import com.mmnaseri.couteau.concurrency.deferred.impl.DefaultDeferred;
import com.mmnaseri.couteau.concurrency.error.TaskContextException;
import com.mmnaseri.couteau.concurrency.error.TaskFailureException;
import com.mmnaseri.couteau.concurrency.manager.TaskManager;
import com.mmnaseri.couteau.concurrency.manager.TaskManagerStatus;
import com.mmnaseri.couteau.concurrency.task.DeferredCallable;
import com.mmnaseri.couteau.concurrency.task.FutureTask;
import com.mmnaseri.couteau.concurrency.task.RetryingTask;
import com.mmnaseri.couteau.concurrency.task.Task;
import com.mmnaseri.couteau.concurrency.task.impl.DelegatedTask;

import java.util.ArrayList;
import java.util.List;

/**
 * This task manager simply executes all scheduled tasks in the given order,
 * without any asynchronous workers.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/8/15, 7:06)
 */
public class IterativeTaskManager implements TaskManager {

    private final List<FutureTask> tasks = new ArrayList<FutureTask>();
    private int scheduled = 0;
    private int done = 0;

    /**
     * Schedules the given task to be executed
     * @param task    the task that is to be executed
     */
    @Override
    public synchronized void schedule(Task task) {
        scheduled ++;
        tasks.add(new DelegatedTask(task));
    }

    @Override
    public <E> Promise<E> defer(final DeferredCallable<E> task) {
        final Deferred<E> deferred = new DefaultDeferred<E>();
        schedule(new Task() {
            @Override
            public void perform() throws TaskFailureException {
                try {
                    deferred.resolve(task.execute());
                } catch (Throwable e) {
                    deferred.reject(e);
                }
            }
        });
        return deferred.getPromise();
    }

    @Override
    public <E> Promise<E> defer(Promise<E> task) {
        return task;
    }

    /**
     * Marks the given task as done within this task manager
     * @param task    the task that was completed
     * @throws TaskContextException if the task has not been scheduled with this task manager
     */
    @Override
    public synchronized void done(Task task) throws TaskContextException {
        if (!(task instanceof FutureTask) || !tasks.contains(task)) {
            throw new TaskContextException(this, task);
        }
        done ++;
        tasks.remove(task);
    }

    /**
     * Signals a failure in carrying out the task
     * @param task         the task that has failed
     * @param exception    the error leading to this failure
     * @throws TaskContextException if the task has not been scheduled with this task manager
     */
    @Override
    public synchronized void fail(Task task, Exception exception) throws TaskContextException {
        done(task);
        if (task instanceof RetryingTask && ((RetryingTask) task).shouldRetry(exception)) {
            schedule(task);
        }
    }

    /**
     * This method will fail, since the iterative task manager does not support
     * any sort of interruption.
     */
    @Override
    public synchronized void shutdown() {
        throw new UnsupportedOperationException("This task manager does not support shutdown");
    }

    @Override
    public TaskManagerStatus getStatus() {
        return new TaskManagerStatus() {
            @Override
            public int getTasks() {
                return scheduled;
            }

            @Override
            public int getDone() {
                return done;
            }

            @Override
            public int getRemaining() {
                return scheduled - done;
            }
        };
    }

    @Override
    public String getName() {
        return "Iterative Task Manager";
    }

    @Override
    public void run() {
        while (!tasks.isEmpty()) {
            final FutureTask task = nextTask();
            if (task == null) {
                throw new IllegalStateException();
            }
            try {
                task.perform();
            } catch (TaskFailureException e) {
                try {
                    fail(task, e);
                } catch (TaskContextException ignored) {
                }
            }
            try {
                done(task);
            } catch (TaskContextException ignored) {
            }
        }
    }

    /**
     * @return the next task to be executed
     */
    private FutureTask nextTask() {
        for (FutureTask task : tasks) {
            if (task.isReady()) {
                return task;
            }
        }
        return null;
    }

}
