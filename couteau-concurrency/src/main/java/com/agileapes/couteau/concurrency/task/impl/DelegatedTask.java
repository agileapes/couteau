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

package com.agileapes.couteau.concurrency.task.impl;

import com.agileapes.couteau.concurrency.error.TaskFailureException;
import com.agileapes.couteau.concurrency.task.FutureTask;
import com.agileapes.couteau.concurrency.task.RetryingTask;
import com.agileapes.couteau.concurrency.task.Task;

/**
 * This is a wrapper for any task that allows for using a singular external
 * interface for objects implementing different aspects of a task.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/15, 5:55)
 */
public class DelegatedTask implements FutureTask, RetryingTask {

    private final Task task;

    /**
     * Instantiates the wrapper
     * @param task    the task to be wrapped
     */
    public DelegatedTask(Task task) {
        this.task = task;
    }

    /**
     * @return if the wrapped task is not an instance of {@link FutureTask} this
     * method will return {@code false}, otherwise it delegates the decision to the
     * wrapped task.
     */
    @Override
    public boolean isReady() {
        return !(task instanceof FutureTask) || ((FutureTask) task).isReady();
    }

    /**
     * Calls to the wrapped task to be performed.
     * @throws TaskFailureException if the wrapped task fails to finish successfully
     */
    @Override
    public void perform() throws TaskFailureException {
        task.perform();
    }

    /**
     * @param failure    the error that has caused the task to fail presently
     * @return if the underlying task does not implement the {@link RetryingTask}
     * interface, this method will return {@code false}, signaling that the task
     * should not be rescheduled for a retry. Otherwise, it will delegate the decision
     * to the underlying task.
     */
    @Override
    public boolean shouldRetry(Exception failure) {
        return task instanceof RetryingTask && ((RetryingTask) task).shouldRetry(failure);
    }

}
