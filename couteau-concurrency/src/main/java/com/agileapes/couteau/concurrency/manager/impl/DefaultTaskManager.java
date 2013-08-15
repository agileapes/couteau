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

import com.agileapes.couteau.concurrency.manager.TaskManager;
import com.agileapes.couteau.concurrency.task.FutureTask;
import com.agileapes.couteau.concurrency.task.RetryingTask;
import com.agileapes.couteau.concurrency.task.Task;
import com.agileapes.couteau.concurrency.task.impl.DelegatedFutureTask;
import com.agileapes.couteau.concurrency.worker.TaskWorker;

import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/15, 6:07)
 */
public class DefaultTaskManager implements TaskManager {

    private final Set<TaskWorker> idle = new HashSet<TaskWorker>();
    private final Map<Task, TaskWorker> working = new HashMap<Task, TaskWorker>();
    private final List<FutureTask> tasks = new ArrayList<FutureTask>();
    private boolean running = true;
    private final String name;
    private final boolean autoShutdown;

    public DefaultTaskManager(String name, int workers, boolean autoShutdown) {
        this.name = name;
        this.autoShutdown = autoShutdown;
        for (int i = 0; i < workers; i ++) {
            final TaskWorker worker = new TaskWorker(this, getName() + ".worker." + i);
            idle.add(worker);
            worker.start();
        }
    }

    @Override
    public void schedule(Task task) {
        synchronized (tasks) {
            tasks.add(new DelegatedFutureTask(task));
        }
    }

    @Override
    public synchronized void done(Task task) {
        if (!working.containsKey(task)) {
            throw new IllegalStateException();
        }
        final TaskWorker worker = working.get(task);
        working.remove(task);
        idle.add(worker);
    }

    @Override
    public void fail(Task task, Exception exception) {
        done(task);
        if (running && ((RetryingTask) task).shouldRetry(exception)) {
            schedule(task);
        }
    }

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

    public String getName() {
        return name;
    }

}
