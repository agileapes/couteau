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
