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

import com.agileapes.couteau.basics.api.impl.StaticStringifiable;
import com.agileapes.couteau.graph.alg.sort.impl.TopologicalGraphSorter;
import com.agileapes.couteau.graph.node.impl.DirectedNode;
import com.agileapes.couteau.maven.mojo.AbstractPluginExecutor;
import com.agileapes.couteau.maven.mojo.PluginExecutorAware;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/3/13, 2:30 PM)
 */
public class TaskScheduler extends Thread implements PluginExecutorAware {

    public static final int DEFAULT_WORKERS = 10;
    private final List<PluginTask<?>> tasks = new CopyOnWriteArrayList<PluginTask<?>>();
    private final List<PluginTask<?>> done = new CopyOnWriteArrayList<PluginTask<?>>();
    private boolean started;
    private final Set<TaskWorker> idle = new CopyOnWriteArraySet<TaskWorker>();
    private final Set<TaskWorker> working = new CopyOnWriteArraySet<TaskWorker>();
    private AbstractPluginExecutor pluginExecutor;
    private boolean running = false;

    public TaskScheduler() {
        this(DEFAULT_WORKERS);
    }

    public TaskScheduler(int workers) {
        super("TaskScheduler");
        started = false;
        for (int i = 0; i < workers; i ++) {
            final TaskWorker worker = new TaskWorker(this);
            idle.add(worker);
            worker.start();
        }
    }

    public void schedule(PluginTask<?> pluginTask) {
        if (started) {
            throw new IllegalStateException();
        }
        tasks.add(pluginTask);
    }

    private void sortTasks() {
        final Map<PluginTask<?>, DirectedNode> nodes = new HashMap<PluginTask<?>, DirectedNode>();
        for (PluginTask<?> task : tasks) {
            final DirectedNode node = new DirectedNode(new StaticStringifiable<DirectedNode>(task.getName()));
            nodes.put(task, node);
            node.setUserData("task", task);
        }
        for (PluginTask<?> task : tasks) {
            final DirectedNode node = nodes.get(task);
            for (PluginTask pluginTask : task.getDependencies()) {
                node.addNeighbor(nodes.get(pluginTask));
            }
        }
        final List<DirectedNode> directedNodes = new TopologicalGraphSorter().sort(nodes.values());
        tasks.clear();
        for (DirectedNode node : directedNodes) {
            tasks.add((PluginTask<?>) node.getUserData("task"));
        }
    }

    @Override
    public void setPluginExecutor(AbstractPluginExecutor pluginExecutor) {
        this.pluginExecutor = pluginExecutor;
    }

    private PluginTask<?> getNextTask() {
        for (PluginTask<?> task : tasks) {
            boolean selected = true;
            for (PluginTask dependency : task.getDependencies()) {
                if (!done.contains(dependency)) {
                    selected = false;
                    break;
                }
            }
            if (selected) {
                return task;
            }
        }
        return null;
    }

    synchronized void done(TaskWorker worker, PluginTask<?> task) {
        working.remove(worker);
        idle.add(worker);
        done.add(task);
    }

    @Override
    public void run() {
        running = true;
        //checking to see if it was started only once
        if (started) {
            throw new IllegalStateException();
        }
        started = true;
        for (TaskWorker worker : idle) {
            worker.setPluginExecutor(pluginExecutor);
        }
        //we sort the task in order of dependencies
        sortTasks();
        //while there are tasks to assign, we will dispatch workers
        while (running && !tasks.isEmpty()) {
            //the next task whose dependencies have been resolved
            final PluginTask<?> next = getNextTask();
            if (next == null || idle.isEmpty()) {
                try {
                    //we wait for a task and a worker to be available
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    fail(e);
                    break;
                }
                continue;
            }
            final TaskWorker worker = idle.iterator().next();
            idle.remove(worker);
            working.add(worker);
            tasks.remove(next);
            //we assign the task to the worker
            worker.assign(next);
            //and notify it
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (worker) {
                worker.notify();
            }
        }
        //while there are workers we wait
        while (!working.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                fail(e);
                break;
            }
        }
        for (TaskWorker worker : idle) {
            worker.close();
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (worker) {
                worker.notify();
            }
        }
    }

    synchronized void fail(Throwable e) {
        shutdown();
        while (!working.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
        throw new Error(e);
    }

    public void shutdown() {
        running = false;
    }

}
