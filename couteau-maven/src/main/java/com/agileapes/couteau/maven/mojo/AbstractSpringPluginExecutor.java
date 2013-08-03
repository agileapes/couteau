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

package com.agileapes.couteau.maven.mojo;

import com.agileapes.couteau.maven.task.PluginTask;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/3/13, 5:45 PM)
 */
public abstract class AbstractSpringPluginExecutor extends AbstractPluginExecutor implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    protected abstract ApplicationContext loadApplicationContext();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected Collection<PluginTask<?>> getTasks() {
        final Map<String,PluginTask> beansOfType = applicationContext.getBeansOfType(PluginTask.class, false, true);
        final HashSet<PluginTask<?>> tasks = new HashSet<PluginTask<?>>();
        for (PluginTask task : beansOfType.values()) {
            tasks.add(task);
        }
        return tasks;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        setApplicationContext(loadApplicationContext());
        //We override the class loader after Spring has found the application context files and relevant beans
        ClassUtils.overrideThreadContextClassLoader(getProjectClassLoader());
        super.execute();
    }
}
