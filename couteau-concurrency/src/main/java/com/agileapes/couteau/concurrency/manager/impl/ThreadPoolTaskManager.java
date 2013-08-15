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
import com.agileapes.couteau.concurrency.manager.TaskManager;
import com.agileapes.couteau.concurrency.task.FutureTask;
import com.agileapes.couteau.concurrency.task.RetryingTask;
import com.agileapes.couteau.concurrency.task.Task;
import com.agileapes.couteau.concurrency.task.impl.DelegatedTask;
import com.agileapes.couteau.concurrency.worker.TaskWorker;

import java.util.*;

/**
 * <p>This task manager will allow for concurrent execution of tasks through the
 * given number of worker threads, effectively managing a thread pool that will
 * be used to cluster the given tasks. By specifying only a single worker thread,
 * the task manager will execute tasks one at a time, effectively becoming
 * synchronous.</p>
 *
 * <p>The task manager's execution does not end as long as the spawned worker
 * threads ({@link TaskWorker}) have not finished their execution.</p>
 *
 * <p>It is expected that the task manager be used by wrapping it in a {@link Thread}
 * instance, and joining with that thread before the application quits.</p>
 *
 * <code><pre>
 *     public static void main(String[] args) throws Exception {
 *         final ThreadPoolTaskManager taskManager = new ThreadPoolTaskManager();
 *         final Thread taskManagerThread = new Thread(taskManager);
 *         taskManagerThread.start();
 *         //perform various tasks that will ultimately result in the scheduling
 *         //of some tasks with the task manager
 *         //shut down the task manager, or, alternatively, use {@link #autoShutdown}
 *         taskManager.shutdown();
 *         //wait for the task manager to finish shutting down gracefully
 *         taskManagerThread.join();
 *     }
 * </pre></code>
 * 
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/15, 6:07)
 */
public class ThreadPoolTaskManager implements TaskManager {

    public static final String TASK_MANAGER_DEFAULT_NAME = "TaskManager";
    /**
     * Idle worker threads
     */
    private final Set<TaskWorker> idle = new HashSet<TaskWorker>();

    /**
     * Worker threads actively assigned a task
     */
    private final Map<Task, TaskWorker> working = new HashMap<Task, TaskWorker>();

    /**
     * Unprocessed tasks
     */
    private final List<FutureTask> tasks = new ArrayList<FutureTask>();

    /**
     * Flag determining whether the task manager is running or not
     */
    private boolean running = true;

    /**
     * The name of this task manager
     */
    private final String name;

    /**
     * <p>Flag determining whether the task manager should quit as soon as there are
     * no more tasks at hand.</p>
     * <p>Setting this to {@code true} requires you to have arranged all the tasks
     * you want done prior to starting the task manager.</p>
     */
    private final boolean autoShutdown;

    /**
     * Instantiates the task manager by setting the number of worker threads to the
     * number of available processors (or processor cores).
     * @see #ThreadPoolTaskManager(int)
     */
    public ThreadPoolTaskManager() {
        this(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Instantiates the task manager by spawning the given number of worker threads
     * @param workers    the desired number of worker threads. Do note that increasing
     *                   this number does not automatically guarantee a better
     *                   performance.
     * @see #ThreadPoolTaskManager(int, boolean)
     */
    public ThreadPoolTaskManager(int workers) {
        this(workers, false);
    }

    /**
     * Instantiates the task manager by specifying the number of worker threads
     * required for execution and setting the auto-shutdown flag. The default name
     * for the task manager will be used.
     * @param workers       the desired number of worker threads. Do note that increasing
     *                      this number does not automatically guarantee a better
     *                      performance.
     * @param autoShutdown  flag determining whether or not auto-shutdown should be
     *                      enabled.
     * @see #TASK_MANAGER_DEFAULT_NAME
     * @see #autoShutdown
     */
    public ThreadPoolTaskManager(int workers, boolean autoShutdown) {
        this(TASK_MANAGER_DEFAULT_NAME, workers, autoShutdown);
    }

    /**
     * Instantiates the task manager, allowing the context to fully configure the
     * manager's workings.
     * @param name          the name of the task manager
     * @param workers       the desired number of worker threads. Do note that increasing
     *                      this number does not automatically guarantee a better
     *                      performance.
     * @param autoShutdown  flag determining whether or not auto-shutdown should be
     *                      enabled.
     */
    public ThreadPoolTaskManager(String name, int workers, boolean autoShutdown) {
        this.name = name;
        this.autoShutdown = autoShutdown;
        for (int i = 0; i < workers; i ++) {
            final TaskWorker worker = new TaskWorker(this, getName() + ".worker." + i);
            idle.add(worker);
            worker.start();
        }
    }

    /**
     * Schedules the given task to be executed
     * @param task    the task that is to be executed
     */
    @Override
    public void schedule(Task task) {
        synchronized (tasks) {
            tasks.add(new DelegatedTask(task));
        }
    }

    /**
     * Marks the given task as done within this task manager
     * @param task    the task that was completed
     * @throws TaskContextException if the task has not been scheduled with this task manager
     */
    @Override
    public synchronized void done(Task task) throws TaskContextException {
        if (!working.containsKey(task)) {
            throw new TaskContextException(this, task);
        }
        final TaskWorker worker = working.get(task);
        working.remove(task);
        idle.add(worker);
    }

    /**
     * Signals a failure in carrying out the task
     * @param task         the task that has failed
     * @param exception    the error leading to this failure
     * @throws TaskContextException if the task has not been scheduled with this task manager
     */
    @Override
    public void fail(Task task, Exception exception) throws TaskContextException {
        done(task);
        if (running && ((RetryingTask) task).shouldRetry(exception)) {
            schedule(task);
        }
    }

    /**
     * Shuts down the task manager gracefully
     */
    @Override
    public synchronized void shutdown() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            synchronized (tasks) {
                if (autoShutdown && tasks.isEmpty()) {
                    shutdown();
                    continue;
                }
            }
            //we wait for a task to become available
            final FutureTask task = nextTask();
            final TaskWorker worker = task != null ? nextWorker(task) : null;
            if (task == null || worker == null) {
                try {
                    //if no task or no free worker is available sleep for some time
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    //if we are interrupted, we should shut down the task manager
                    shutdown();
                }
                continue;
            }
            //a task is available for performing
            worker.assign(task);
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (worker) {
                //we tell the worker to wake up
                worker.notify();
            }
        }
        //we have been told to shut the manager down
        //we first wait for active workers to gracefully finish
        while (!working.isEmpty()) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException ignored) {
            }
        }
        //we then dismiss all idle workers
        for (TaskWorker worker : idle) {
            worker.dismiss();
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (worker) {
                worker.notify();
            }
        }
        for (TaskWorker worker : idle) {
            try {
                worker.join();
            } catch (InterruptedException ignored) {
            }
        }
    }

    private FutureTask nextTask() {
        for (FutureTask task : tasks) {
            if (task.isReady()) {
                return task;
            }
        }
        return null;
    }

    private TaskWorker nextWorker(FutureTask task) {
        synchronized (idle) {
            if (!idle.isEmpty()) {
                final TaskWorker worker = idle.iterator().next();
                idle.remove(worker);
                synchronized (tasks) {
                    tasks.remove(task);
                }
                synchronized (working) {
                    working.put(task, worker);
                }
                return worker;
            }
            return null;
        }
    }

    /**
     * @return the name associated with this task manager
     */
    public String getName() {
        return name;
    }

}
