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

package com.agileapes.couteau.concurrency.worker;

import com.agileapes.couteau.concurrency.error.TaskFailureException;
import com.agileapes.couteau.concurrency.manager.TaskManager;
import com.agileapes.couteau.concurrency.task.Task;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/15, 5:58)
 */
public class TaskWorker extends Thread {

    private Task task = null;
    private boolean dismissed = false;
    private final TaskManager taskManager;

    public TaskWorker(TaskManager taskManager, String name) {
        super(name);
        this.taskManager = taskManager;
    }

    public void assign(Task task) {
        this.task = task;
    }

    public void dismiss() {
        dismissed = true;
    }

    @Override
    public void run() {
        while (!dismissed) {
            //the worker waits until it is notified of some sort of
            //change in the atmosphere
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                if (task != null) {
                    //if it is interrupted while waiting, and if it has been assigned
                    //a task, the task manager must be notified of this failure
                    taskManager.fail(task, e);
                    continue;
                }
            }
            if (dismissed) {
                //if the worker was dismissed while it was in waiting,
                //it must quit.
                continue;
            }
            if (task == null) {
                //if the worker was somehow notified of a change, but no
                //task was assigned to it, nor was it dismissed, then we
                //are in unknown situation, in which we will return to waiting
                //for either a task or a clear dismissal to arrive
                continue;
            }
            try {
                //we try to perform the task
                task.perform();
            } catch (TaskFailureException e) {
                //if it failed to complete, we notify the task manager
                taskManager.fail(task, e);
                task = null;
                continue;
            }
            //if the task completed successfully, we tell the task manager as such
            taskManager.done(task);
            //we nullify the task to relieve the worker of the burden
            task = null;
        }
    }

    @Override
    public String toString() {
        return getName() + "[" + task + "]";
    }
}
