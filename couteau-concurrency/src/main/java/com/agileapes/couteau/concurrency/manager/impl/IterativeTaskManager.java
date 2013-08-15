/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.couteau.concurrency.manager.impl;

import com.agileapes.couteau.concurrency.error.TaskContextException;
import com.agileapes.couteau.concurrency.error.TaskFailureException;
import com.agileapes.couteau.concurrency.manager.TaskManager;
import com.agileapes.couteau.concurrency.task.FutureTask;
import com.agileapes.couteau.concurrency.task.RetryingTask;
import com.agileapes.couteau.concurrency.task.Task;
import com.agileapes.couteau.concurrency.task.impl.DelegatedTask;

import java.util.ArrayList;
import java.util.List;

/**
 * This task manager simply executes all scheduled tasks in the given order,
 * without any asynchronous workers.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/15, 7:06)
 */
public class IterativeTaskManager implements TaskManager {

    private final List<FutureTask> tasks = new ArrayList<FutureTask>();

    /**
     * Schedules the given task to be executed
     * @param task    the task that is to be executed
     */
    @Override
    public synchronized void schedule(Task task) {
        tasks.add(new DelegatedTask(task));
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
