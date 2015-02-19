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

package com.agileapes.couteau.maven.mojo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * This bean should be used to enable the injection of the plugin executor into the requesting beans
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/15/13, 1:23 PM)
 */
@Component
public class SpringPluginConfigurator implements BeanPostProcessor {

    private AbstractSpringPluginExecutor pluginExecutor;
    private Set<PluginExecutorAware> executorAwareBeans = new CopyOnWriteArraySet<PluginExecutorAware>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof PluginExecutorAware) {
            if (pluginExecutor != null) {
                //noinspection unchecked
                ((PluginExecutorAware) bean).setPluginExecutor(pluginExecutor);
            } else {
                executorAwareBeans.add((PluginExecutorAware) bean);
            }
        }
        return bean;
    }

    public void setPluginExecutor(AbstractSpringPluginExecutor pluginExecutor) {
        this.pluginExecutor = pluginExecutor;
        for (PluginExecutorAware bean : executorAwareBeans) {
            //noinspection unchecked
            bean.setPluginExecutor(pluginExecutor);
        }
        executorAwareBeans.clear();
    }

}
