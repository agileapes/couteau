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

import com.agileapes.couteau.concurrency.error.TaskCompletionFailedException;
import com.agileapes.couteau.concurrency.error.TaskContextException;
import com.agileapes.couteau.concurrency.error.TaskFailureException;
import com.agileapes.couteau.concurrency.manager.TaskManager;
import com.agileapes.couteau.concurrency.task.Task;

/**
 * This class extends the {@link Thread} class to allow for easy assignment of tasks. The TaskWorker will take a task from
 * the owning task manager and will try to carry it out successfully.
 *
 * @see #assign(Task)
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/15, 5:58)
 */
public class TaskWorker extends Thread {

    /**
     * The task assigned to the worker, or {@code null} if no task has been assigned to the worker, yet
     */
    private Task task = null;

    /**
     * Flag determining whether the worker has been dismissed
     */
    private boolean dismissed = false;

    /**
     * The task manager within which this worker is being executed
     */
    private final TaskManager taskManager;

    /**
     * Instantiates the worker by assigning the task manager and giving it a name
     * @param taskManager    the task manager spawning this worker thread
     * @param name           the name of this worker
     */
    public TaskWorker(TaskManager taskManager, String name) {
        super(name);
        this.taskManager = taskManager;
    }

    /**
     * Assigns a new task to the worker thread
     * @param task    the task to be carried out
     */
    public void assign(Task task) {
        if (this.task != null) {
            try {
                taskManager.fail(this.task, new TaskCompletionFailedException("The worker was assigned a new task before it could finish its previous task"));
            } catch (TaskContextException e) {
                throw new IllegalStateException();
            }
        }
        this.task = task;
    }

    /**
     * Dismisses the worker by asking it to finish execution
     */
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
                    try {
                        taskManager.fail(task, e);
                    } catch (TaskContextException contextException) {
                        //context exceptions are not tolerated at this level
                        break;
                    }
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
                try {
                    taskManager.fail(task, e);
                } catch (TaskContextException contextException) {
                    //context exceptions are not tolerated at this level
                    break;
                }
                task = null;
                continue;
            }
            //if the task completed successfully, we tell the task manager as such
            try {
                //context exceptions are not tolerated at this level
                taskManager.done(task);
            } catch (TaskContextException contextException) {
                break;
            }
            //we nullify the task to relieve the worker of the burden
            task = null;
        }
    }

    @Override
    public String toString() {
        return getName() + "[" + task + "]";
    }
}
