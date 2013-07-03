package com.agileapes.couteau.context.spring;

import com.agileapes.couteau.context.contract.Context;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * This interface indicates that the context needs to be configured to be used with Spring.
 * This way, the contexts will be easily picked up by the configurator and added to Spring's
 * context the correct way.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/3/13, 6:39 PM)
 */
public interface SpringConfigurableContext<E> extends Context<E> {

    /**
     * This method will be called to give the context a one-time-only chance to configure
     * and prepare itself for usage within Spring's context.
     * @param beanFactory    the bean factory within which the context is being configured
     */
    void configure(ConfigurableListableBeanFactory beanFactory);

}
