/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
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

package com.mmnaseri.couteau.concurrency.worker;

import com.mmnaseri.couteau.concurrency.error.TaskCompletionFailedException;
import com.mmnaseri.couteau.concurrency.error.TaskContextException;
import com.mmnaseri.couteau.concurrency.error.TaskFailureException;
import com.mmnaseri.couteau.concurrency.manager.TaskManager;
import com.mmnaseri.couteau.concurrency.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class extends the {@link Thread} class to allow for easy assignment of tasks. The TaskWorker will take a task from
 * the owning task manager and will try to carry it out successfully.
 *
 * @see #assign(Task)
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/8/15, 5:58)
 */
public class TaskWorker extends Thread {

    private static final Log LOG = LogFactory.getLog(TaskWorker.class);

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
        LOG.info("Initializing worker " + name + " for task manager " + taskManager.getName());
        this.taskManager = taskManager;
    }

    /**
     * Assigns a new task to the worker thread
     * @param task    the task to be carried out
     */
    public void assign(Task task) {
        LOG.info("New task received.");
        if (this.task != null) {
            try {
                LOG.error("Worker already busy with another task");
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
        LOG.info("Worker dismissed externally.");
        dismissed = true;
    }

    @Override
    public void run() {
        LOG.info("Starting up the worker thread");
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
                        LOG.error("Task worker was interrupted while being assigned a task. Notifying the task manager.");
                        taskManager.fail(task, e);
                    } catch (TaskContextException contextException) {
                        //context exceptions are not tolerated at this level
                        break;
                    }
                    continue;
                }
            }
            if (dismissed) {
                LOG.info("Task worker dismissed while waiting for a task.");
                //if the worker was dismissed while it was in waiting,
                //it must quit.
                continue;
            }
            if (task == null) {
                LOG.warn("Task worker woke up, only to find nothing waiting.");
                //if the worker was somehow notified of a change, but no
                //task was assigned to it, nor was it dismissed, then we
                //are in unknown situation, in which we will return to waiting
                //for either a task or a clear dismissal to arrive
                continue;
            }
            try {
                //we try to perform the task
                LOG.info("Performing assigned task");
                final long time = System.currentTimeMillis();
                task.perform();
                LOG.info("Finished performing the task in " + (System.currentTimeMillis() - time) + " milliseconds");
            } catch (TaskFailureException e) {
                //if it failed to complete, we notify the task manager
                try {
                    LOG.error("Task execution failed. Notifying the task manager");
                    LOG.debug(e);
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
                LOG.info("Notifying the task manager of successful task execution");
                //context exceptions are not tolerated at this level
                taskManager.done(task);
            } catch (TaskContextException contextException) {
                break;
            }
            LOG.trace("Going back to sleep and waiting for a new task to be assigned by the task manager.");
            //we nullify the task to relieve the worker of the burden
            task = null;
        }
    }

    @Override
    public String toString() {
        return getName() + "[" + task + "]";
    }
}
