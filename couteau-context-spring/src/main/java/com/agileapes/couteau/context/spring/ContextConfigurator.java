package com.agileapes.couteau.context.spring;

import com.agileapes.couteau.context.contract.Context;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/30, 10:35)
 */
public interface ContextConfigurator<C extends Context> {

    void configure(C context, ConfigurableListableBeanFactory beanFactory);

}
