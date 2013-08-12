package com.agileapes.couteau.maven.task;

import com.agileapes.couteau.maven.mojo.AbstractPluginExecutor;
import org.apache.maven.plugin.MojoFailureException;
import org.springframework.beans.factory.BeanNameAware;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/2/13, 10:58 AM)
 */
public abstract class PluginTask<E extends AbstractPluginExecutor> implements BeanNameAware {

    private static long lastIdentifier = 0;

    private long identifier;
    private String name;
    private List<PluginTask> children = new ArrayList<PluginTask>();
    private Set<PluginTask> dependencies = new CopyOnWriteArraySet<PluginTask>();
    private PluginTask parent;

    protected PluginTask() {
        identifier = lastIdentifier ++;
        name = getClass().getCanonicalName();
    }

    public List<PluginTask> getChildren() {
        return children;
    }

    /**
     * Sets the children of this task. It will also update the dependencies, in a way that
     * each child task is a also a dependency of this task.
     * @param children    the new list of children
     */
    public void setChildren(List<PluginTask> children) {
        this.children = children;
        prepareTasks();
        for (PluginTask child : this.children) {
            child.setParent(this);
        }
    }

    public Set<PluginTask> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<PluginTask> dependencies) {
        this.dependencies = dependencies;
        prepareTasks();
    }

    protected PluginTask getParent() {
        return parent;
    }

    public void setParent(PluginTask parent) {
        if (parent != null && !parent.getChildren().contains(this)) {
            throw new IllegalStateException();
        }
        this.parent = parent;
    }

    private void prepareTasks() {
        if (children == null) {
            children = new ArrayList<PluginTask>();
        }
        if (dependencies == null) {
            dependencies = new HashSet<PluginTask>();
        }
        dependencies.addAll(children);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginTask that = (PluginTask) o;
        return identifier == that.identifier;

    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }

    public long getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public abstract void execute(E executor) throws MojoFailureException;

}
