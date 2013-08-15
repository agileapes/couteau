package com.agileapes.couteau.maven.task;

import com.agileapes.couteau.concurrency.error.TaskFailureException;
import com.agileapes.couteau.concurrency.task.FutureTask;
import com.agileapes.couteau.maven.mojo.AbstractPluginExecutor;
import com.agileapes.couteau.maven.mojo.PluginExecutorAware;
import org.apache.maven.plugin.MojoFailureException;
import org.springframework.beans.factory.BeanNameAware;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/2/13, 10:58 AM)
 */
public abstract class PluginTask<E extends AbstractPluginExecutor> implements BeanNameAware, FutureTask, PluginExecutorAware<E> {

    private static long lastIdentifier = 0;

    private long identifier;
    private String name;
    private final Collection<PluginTask<E>> children = new CopyOnWriteArraySet<PluginTask<E>>();
    private final Collection<PluginTask<E>> dependencies = new CopyOnWriteArraySet<PluginTask<E>>();
    private final Collection<PluginTask<E>> prerequisiteFor = new CopyOnWriteArraySet<PluginTask<E>>();
    private PluginTask parent;
    private E pluginExecutor;

    public PluginTask() {
        identifier = lastIdentifier ++;
        name = getClass().getCanonicalName();
    }

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

    public final Collection<PluginTask<E>> getDependencies() {
        return Collections.unmodifiableCollection(dependencies);
    }

    public final void setDependencies(Collection<PluginTask<E>> dependencies) {
        for (PluginTask<E> dependency : dependencies) {
            addDependency(dependency);
        }
    }

    protected final PluginTask getParent() {
        return parent;
    }

    public void setParent(PluginTask parent) {
        if (parent != null && !parent.getChildren().contains(this)) {
            throw new IllegalStateException();
        }
        this.parent = parent;
    }

    private void addDependency(PluginTask<E> dependency) {
        dependencies.add(dependency);
        dependency.markAsPrerequisiteFor(this);
    }

    private void addChild(PluginTask<E> child) {
        children.add(child);
        addDependency(child);
        child.setParent(this);
    }

    private void markAsPrerequisiteFor(PluginTask<E> task) {
        prerequisiteFor.add(task);
    }

    private void resolveDependency(PluginTask<E> dependency) {
        dependencies.remove(dependency);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginTask that = (PluginTask) o;
        return identifier == that.identifier;

    }

    @Override
    public final void setBeanName(String name) {
        this.name = name;
    }

    public final long getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    @Override
    public final void setPluginExecutor(E pluginExecutor) {
        this.pluginExecutor = pluginExecutor;
    }

    @Override
    public final boolean isReady() {
        return dependencies.isEmpty();
    }

    @Override
    public final void perform() throws TaskFailureException {
        try {
            execute(pluginExecutor);
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

    public abstract void execute(E executor) throws MojoFailureException;

}
