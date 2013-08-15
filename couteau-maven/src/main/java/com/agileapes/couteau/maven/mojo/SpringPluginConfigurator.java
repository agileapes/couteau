package com.agileapes.couteau.maven.mojo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * This bean should be used to enable the injection of the plugin executor into the requesting beans
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/15/13, 1:23 PM)
 */
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
