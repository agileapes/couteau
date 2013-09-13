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
 * This interface extends the {@link Task} interface to allow the task to
 * specify whether or not it is now ready to be performed.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/15, 5:55)
 */
public interface FutureTask extends Task {

    /**
     * Should determine whether or not the given task is now to be executed.
     * @return {@code true} will mean that the task can now be performed, i.e. all
     * of its prerequisites have been met.
     */
    boolean isReady();

}