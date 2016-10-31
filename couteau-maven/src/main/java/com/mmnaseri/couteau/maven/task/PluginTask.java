/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
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

package com.mmnaseri.couteau.maven.task;

import com.mmnaseri.couteau.maven.mojo.AbstractPluginExecutor;
import com.mmnaseri.couteau.maven.mojo.PluginExecutorAware;
import com.mmnaseri.couteau.concurrency.error.TaskFailureException;
import com.mmnaseri.couteau.concurrency.task.FutureTask;
import org.apache.maven.plugin.MojoFailureException;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (8/2/13, 10:58 AM)
 */
public abstract class PluginTask<E extends AbstractPluginExecutor> implements FutureTask, PluginExecutorAware<E> {

    /**
     * Holds the unique identifier to each instance
     */
    private static long lastIdentifier = 0;

    /**
     * This instance's unique identifier
     */
    private long identifier;

    /**
     * The name of this task
     */
    private String name;

    /**
     * This task's children
     */
    private final Collection<PluginTask<E>> children = new CopyOnWriteArraySet<PluginTask<E>>();

    /**
     * This tasks direct dependencies
     */
    private final Collection<PluginTask<E>> dependencies = new CopyOnWriteArraySet<PluginTask<E>>();

    /**
     * Tasks that depend on the completion of this task
     */
    private final Collection<PluginTask<E>> prerequisiteFor = new CopyOnWriteArraySet<PluginTask<E>>();

    /**
     * The parent for this task
     */
    private PluginTask parent;

    /**
     * The plugin executor running this task
     */
    private E pluginExecutor;

    /**
     * Instantiates the task
     */
    public PluginTask() {
        identifier = lastIdentifier ++;
        name = getClass().getCanonicalName();
    }

    /**
     * @return an unmodifiable collection representing this task's children
     */
    public final Collection<PluginTask<E>> getChildren() {
        return Collections.unmodifiableCollection(children);
    }

    /**
     * Sets the children of this task. It will also update the dependencies, in a way that
     * each child task is a also a dependency of this task.
     * @param children    the new list of children
     */
    public final void setChildren(Collection<PluginTask<E>> children) {
        for (PluginTask<E> child : children) {
            addChild(child);
        }
    }

    /**
     * @return an unmodifiable collection of tasks representing tasks upon whose completion this
     * task depends
     */
    public final Collection<PluginTask<E>> getDependencies() {
        return Collections.unmodifiableCollection(dependencies);
    }

    /**
     * Updates this task's dependencies by overriding them with the new set of dependencies
     * @param dependencies    the new set of dependencies for this task
     */
    public void setDependencies(Collection<PluginTask<E>> dependencies) {
        for (PluginTask<E> dependency : dependencies) {
            addDependency(dependency);
        }
    }

    /**
     * @return this task's parent or {@code null} if this is a root task
     */
    protected final PluginTask getParent() {
        return parent;
    }

    /**
     * Changes this task's parent task
     * @param parent    the new parent
     */
    private void setParent(PluginTask parent) {
        if (parent != null && !parent.getChildren().contains(this)) {
            throw new IllegalStateException();
        }
        this.parent = parent;
    }

    /**
     * Adds a dependency to this task's collection of dependencies. Also, tells the dependency task
     * that it is a prerequisite for this task.
     * @param dependency    a new dependency for this task
     */
    private void addDependency(PluginTask<E> dependency) {
        dependencies.add(dependency);
        dependency.markAsPrerequisiteFor(this);
    }

    /**
     * Adds a child to this task's collection of children, also marking it as a dependency
     * @param child    a new child for this task
     * @see #addDependency(PluginTask)
     */
    private void addChild(PluginTask<E> child) {
        children.add(child);
        addDependency(child);
        child.setParent(this);
    }

    /**
     * Marks this task as a dependency of the given task
     * @param task    the task
     */
    private void markAsPrerequisiteFor(PluginTask<E> task) {
        prerequisiteFor.add(task);
    }

    /**
     * Marks the given dependency as resolved
     * @param dependency    the dependency to be resolved
     */
    private void resolveDependency(PluginTask<E> dependency) {
        dependencies.remove(dependency);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginTask that = (PluginTask) o;
        return identifier == that.getIdentifier();

    }

    public final void setName(String name) {
        this.name = name;
    }

    /**
     * @return this task's unique identifier
     */
    public final long getIdentifier() {
        return identifier;
    }

    /**
     * @return this task's human-friendly name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes this task's plugin executor
     * @param pluginExecutor    the executor
     */
    @Override
    public final void setPluginExecutor(E pluginExecutor) {
        this.pluginExecutor = pluginExecutor;
    }

    /**
     * @return when all dependencies have been resolved, signals this task as ready to
     * be executed.
     */
    @Override
    public final boolean isReady() {
        return dependencies.isEmpty();
    }

    /**
     * Calls to {@link #execute(AbstractPluginExecutor)}, upon the successful completion of which
     * it will then proceed to mark itself as resolved for all tasks which depend on this task.
     * @throws TaskFailureException
     */
    @Override
    public final void perform() throws TaskFailureException {
        final String intro = getIntro();
        final String outro = getOutro();
        try {
            if (intro != null && !intro.isEmpty()) {
                pluginExecutor.getLog().info(intro);
            }
            execute(pluginExecutor);
            if (outro != null && !outro.isEmpty()) {
                pluginExecutor.getLog().info(outro);
            }
        } catch (MojoFailureException e) {
            throw new TaskFailureException("Failed to execute task", e);
        }
        for (PluginTask<E> task : prerequisiteFor) {
            task.resolveDependency(this);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Contains instructions as to what this task truly entails
     * @param executor    the executor in charge of the current execution
     * @throws MojoFailureException if anything prevents the plugin task from completing successfullly
     */
    public abstract void execute(E executor) throws MojoFailureException;

    protected String getIntro() {
        return null;
    }

    protected String getOutro() {
        return null;
    }

}
