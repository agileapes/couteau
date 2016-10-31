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

package com.mmnaseri.couteau.maven.mojo;

import com.mmnaseri.couteau.maven.task.PluginTask;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * <p>This class extends the functionalities of the {@link AbstractPluginExecutor} to enable you to load
 * task definitions from a Spring {@link ApplicationContext}. To that end, in contrast with the original
 * base class, the {@link #getTasks()} methods has already been configured to look through the loaded
 * {@link ApplicationContext} for task definitions.</p>
 * <p>Moreover, Spring bean names are now used as task names. This helps with the unification of bean
 * identification, and reduces confusion as to the naming of tasks.</p>
 * <p>If you have beans other than your tasks which require access to the plugin executor, it is advisable
 * that you should add a bean definition for {@link SpringPluginConfigurator} to your application context.</p>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/3/13, 5:45 PM)
 */
public abstract class AbstractSpringPluginExecutor extends AbstractPluginExecutor implements ApplicationContextAware {

    /**
     * The application context loaded for this plugin executor
     */
    private ApplicationContext applicationContext;

    /**
     * Loads the application context through which the plugin is expected to work
     * @return the loaded and configured application context, containing task definitions
     */
    protected abstract ApplicationContext loadApplicationContext();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * @return task definitions found in the application context. Note that only singleton beans are
     * loaded, and that these beans will be eagerly initialized for the purpose of this plugin.
     */
    @Override
    protected Collection<PluginTask<?>> getTasks() {
        final Map<String,PluginTask> beansOfType = applicationContext.getBeansOfType(PluginTask.class, false, true);
        final HashSet<PluginTask<?>> tasks = new HashSet<PluginTask<?>>();
        for (Map.Entry<String, PluginTask> entry : beansOfType.entrySet()) {
            final PluginTask task = entry.getValue();
            task.setName(entry.getKey());
            tasks.add(task);
        }
        return tasks;
    }

    /**
     * Tries to locate the {@link SpringPluginConfigurator} instance and thus configure other beans indirectly
     */
    private void preparePlugin() {
        final String[] beanNamesForType = applicationContext.getBeanNamesForType(SpringPluginConfigurator.class);
        if (beanNamesForType.length != 0) {
            final String beanName = beanNamesForType[0];
            final SpringPluginConfigurator configurator = applicationContext.getBean(beanName, SpringPluginConfigurator.class);
            configurator.setPluginExecutor(this);
        }
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        setApplicationContext(loadApplicationContext());
        preparePlugin();
        super.execute();
    }

}
