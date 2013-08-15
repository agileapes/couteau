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

package com.agileapes.couteau.concurrency.task;

/**
 * This interface extends the {@link Task} interface to allow for the task to
 * handle failures by telling the {@link com.agileapes.couteau.concurrency.manager.TaskManager}
 * whether or not it should retry performing the task by rescheduling it.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/15, 6:16)
 */
public interface RetryingTask extends Task {

    /**
     * This method will determine whether or not the task should be retried based
     * on the given error
     * @param failure    the error causing the failure
     * @return {@code true} means that the task should be scheduled once more to be
     * tried out
     */
    boolean shouldRetry(Exception failure);

}
