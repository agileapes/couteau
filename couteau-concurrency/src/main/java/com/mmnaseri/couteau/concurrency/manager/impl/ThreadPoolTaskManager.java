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

package com.mmnaseri.couteau.concurrency.manager.impl;

import com.mmnaseri.couteau.basics.api.Processor;
import com.mmnaseri.couteau.concurrency.deferred.Deferred;
import com.mmnaseri.couteau.concurrency.deferred.Failure;
import com.mmnaseri.couteau.concurrency.deferred.Promise;
import com.mmnaseri.couteau.concurrency.deferred.impl.DefaultDeferred;
import com.mmnaseri.couteau.concurrency.error.TaskContextException;
import com.mmnaseri.couteau.concurrency.error.TaskFailureException;
import com.mmnaseri.couteau.concurrency.manager.TaskManager;
import com.mmnaseri.couteau.concurrency.manager.TaskManagerStatus;
import com.mmnaseri.couteau.concurrency.task.*;
import com.mmnaseri.couteau.concurrency.task.impl.DelegatedTask;
import com.mmnaseri.couteau.concurrency.worker.TaskWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * <p><strong>Example:</strong></p>
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
 * <p>To control that the task manager quits properly we can easily monitor the number of remaining tasks
 * and deduce if we should shutdown the task manager once it has no more tasks to perform. This should be
 * done <em>before</em> <code>join</code>ing the task manager's thread with its parent thread.</p>
 *
 * <p>See {@link #getStatus() status} for more details.</p>
 *
 * <p><strong>Example:</strong></p>
 *
 * <code><pre>
 *     //:
 *     taskManagerThread.start();
 *     boolean shutdown = false;
 *     while (!shutdown) {
 *          while (taskManager.getStatus().getRemaining() > 0) {
 *             Thread.sleep(200);
 *          }
 *          if (weShouldShutdown(taskManager)) {
 *              taskManager.shutdown();
 *              shutdown = true;
 *          }
 *     }
 *     //:
 * </pre></code>
 *
 * <p>In the above example, the <code>weShouldShutdown(...)</code> method determines whether or not the fact that
 * the task manager currently has no more scheduled tasks to perform should be taken as a hint that the task manager
 * should be shut gracefully down and thus barring the possibility of it accepting any more tasks.</p>
 *
 * <p>It is worthy of note that in a large application, at times the fact that a task manager has no more tasks to
 * perform does not <em>necessarily</em> indicate that it will not, at some point of time in the future, receive
 * more tasks to perform, and as such should not be shutdown just yet.</p>
 *
 * <p>The {@link #autoShutdown} flag also relies heavily on the same mechanics and as such for non-trivial cases
 * wherein the task manager might be forced to wait upon an entry channel for tasks to arrive you are advised
 * against using the convenience flag and rather writing your own monitoring and shutdown mechanism.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/8/15, 6:07)
 */
public class ThreadPoolTaskManager implements TaskManager {

    private final static Log LOG = LogFactory.getLog(TaskManager.class);

    public static final String TASK_MANAGER_DEFAULT_NAME = "TaskManager";
    /**
     * Idle worker threads
     */
    private final List<TaskWorker> idle = new ArrayList<TaskWorker>();

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

    private int scheduled = 0;
    private int done = 0;

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
        LOG.info("Initializing task manager " + name + " with " + workers + " worker(s)");
        if (autoShutdown) {
            LOG.trace("Auto-shutdown is set to true");
        }
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
        LOG.info("Scheduling a new task");
        synchronized (tasks) {
            if (task instanceof Prioritized) {
                tasks.add(0, new DelegatedTask(task));
            } else {
                tasks.add(new DelegatedTask(task));
            }
            scheduled ++;
        }
    }

    @Override
    public <E> Promise<E> defer(final DeferredCallable<E> task) {
        LOG.info("Deferring a task to be executed at a later time");
        final Deferred<E> deferred = new DefaultDeferred<E>();
        final Task callable = new Task() {
            @Override
            public void perform() throws TaskFailureException {
                try {
                    LOG.trace("Executing deferred task");
                    deferred.resolve(task.execute());
                } catch (Throwable e) {
                    LOG.error("Deferred task failed");
                    LOG.error(e);
                    deferred.reject(e);
                }
            }
        };
        if (task instanceof Prioritized) {
            schedule(new PrioritizedTask() {
                @Override
                public void perform() throws TaskFailureException {
                    callable.perform();
                }
            });
        } else {
            schedule(callable);
        }
        return deferred.getPromise();
    }

    @Override
    public <E> Promise<E> defer(final Promise<E> task) {
        final DefaultDeferred<E> deferred = new DefaultDeferred<E>();
        LOG.info("Deferring a promised task for a later time");
        schedule(new Task() {
            @Override
            public void perform() throws TaskFailureException {
                task.then(new Processor<E>() {
                    @Override
                    public void process(E result) {
                        deferred.resolve(result);
                    }
                }, new Processor<Failure>() {
                    @Override
                    public void process(Failure failure) {
                        deferred.reject(failure);
                    }
                });
            }
        });
        return deferred.getPromise();
    }

    /**
     * Marks the given task as done within this task manager
     * @param task    the task that was completed
     * @throws TaskContextException if the task has not been scheduled with this task manager
     */
    @Override
    public synchronized void done(Task task) throws TaskContextException {
        LOG.info("Task finished. Removing task from queue.");
        if (!working.containsKey(task)) {
            throw new TaskContextException(this, task);
        }
        final TaskWorker worker = working.get(task);
        working.remove(task);
        idle.add(worker);
        done ++;
    }

    /**
     * Signals a failure in carrying out the task
     * @param task         the task that has failed
     * @param exception    the error leading to this failure
     * @throws TaskContextException if the task has not been scheduled with this task manager
     */
    @Override
    public void fail(Task task, Exception exception) throws TaskContextException {
        LOG.warn("Failed to carry out task properly.");
        LOG.debug(exception);
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
        LOG.info("Requesting shutdown for task manager: " + getName());
        running = false;
    }

    @Override
    public TaskManagerStatus getStatus() {
        return new TaskManagerStatus() {

            @Override
            public int getTasks() {
                return scheduled;
            }

            @Override
            public int getDone() {
                return done;
            }

            @Override
            public int getRemaining() {
                return scheduled - done;
            }

        };
    }

    @Override
    public void run() {
        LOG.info("Starting to process task incoming queue");
        if (tasks.isEmpty() && autoShutdown) {
            LOG.warn("Task manager is in auto-shutdown mode and no tasks have been scheduled.");
        }
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
                    LOG.error("Task manager was interrupted externally. Shutting down.");
                    LOG.debug(e);
                    //if we are interrupted, we should shut down the task manager
                    shutdown();
                }
                continue;
            }
            //a task is available for performing
            LOG.info("Assigning the task to worker " + worker.getName());
            worker.assign(task);
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (worker) {
                LOG.debug("Notifying the worker to perform the task");
                //we tell the worker to wake up
                worker.notify();
            }
        }
        LOG.info("Waiting for all works in progress to finish gracefully ...");
        //we have been told to shut the manager down
        //we first wait for active workers to gracefully finish
        while (!working.isEmpty()) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException ignored) {
            }
        }
        LOG.info("Dismissing idle workers ...");
        //we then dismiss all idle workers
        for (TaskWorker worker : idle) {
            LOG.trace("Dismissing worker " + worker.getName());
            worker.dismiss();
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (worker) {
                worker.notify();
            }
        }
        LOG.info("Waiting for all worker threads to gather together");
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
                LOG.trace("Picking a task to be executed.");
                return task;
            }
        }
        LOG.trace("No tasks are ready to be executed yet.");
        return null;
    }

    private synchronized TaskWorker nextWorker(FutureTask task) {
        if (!idle.isEmpty()) {
            final TaskWorker worker = idle.get(0);
            idle.remove(worker);
            working.put(task, worker);
            tasks.remove(task);
            LOG.trace("Found a worker to perform the task");
            return worker;
        }
        LOG.trace("No idle workers to perform the task were found");
        return null;
    }

    /**
     * @return the name associated with this task manager
     */
    @Override
    public String getName() {
        return name;
    }

}
