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
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/15, 5:55)
 */
public class DelegatedFutureTask implements FutureTask, RetryingTask {

    private final Task task;

    public DelegatedFutureTask(Task task) {
        this.task = task;
    }

    @Override
    public boolean isReady() {
        return !(task instanceof FutureTask) || ((FutureTask) task).isReady();
    }

    @Override
    public void perform() throws TaskFailureException {
        task.perform();
    }

    @Override
    public boolean shouldRetry(Exception failure) {
        return task instanceof RetryingTask && ((RetryingTask) task).shouldRetry(failure);
    }

}
