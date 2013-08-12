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

package com.agileapes.couteau.maven.task;

import com.agileapes.couteau.maven.mojo.AbstractPluginExecutor;
import com.agileapes.couteau.maven.mojo.PluginExecutorAware;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/3/13, 3:11 PM)
 */
public class TaskWorker extends Thread implements PluginExecutorAware {

    private static long identifier = 0;
    private PluginTask task;
    private boolean stopped = false;
    private AbstractPluginExecutor pluginExecutor;
    private final TaskScheduler taskScheduler;

    public TaskWorker(TaskScheduler taskScheduler) {
        super("taskWorker." + (identifier ++));
        this.taskScheduler = taskScheduler;
    }

    public void assign(PluginTask<?> task) {
        this.task = task;
    }

    public void close() {
        stopped = true;
    }

    @Override
    public void setPluginExecutor(AbstractPluginExecutor pluginExecutor) {
        this.pluginExecutor = pluginExecutor;
    }

    @Override
    public void run() {
        synchronized (this) {
            while (true) {
                if (stopped) {
                    break;
                }
                try {
                    wait();
                } catch (InterruptedException ignored) {
                    System.err.println("error");
                }
                if (task == null) {
                    continue;
                }
                try {
                    //noinspection unchecked
                    task.execute(pluginExecutor);
                } catch (Throwable e) {
                    taskScheduler.fail(e);
                }
                taskScheduler.done(this, task);
                task = null;
            }
        }
    }

    @Override
    public String toString() {
        return getName();
    }

}
