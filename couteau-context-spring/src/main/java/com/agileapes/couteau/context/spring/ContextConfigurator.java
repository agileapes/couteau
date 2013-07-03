package com.agileapes.couteau.context.spring;

import com.agileapes.couteau.context.contract.Context;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * This interface allows implementation of callbacks that will allow configuration of contexts
 * within a bean factory
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/30, 10:35)
 */
public interface ContextConfigurator<C extends Context> {

    public static final ContextConfigurator<Context<?>> NULL = new ContextConfigurator<Context<?>>() {
        @Override
        public void configure(Context<?> context, ConfigurableListableBeanFactory beanFactory) {
        }
    };

    void configure(C context, ConfigurableListableBeanFactory beanFactory);

}
