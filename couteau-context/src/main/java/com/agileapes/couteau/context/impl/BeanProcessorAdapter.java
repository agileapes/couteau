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

package com.agileapes.couteau.context.impl;

import com.agileapes.couteau.context.contract.BeanProcessor;
import com.agileapes.couteau.context.contract.OrderedBean;
import com.agileapes.couteau.context.error.RegistryException;

/**
 * This adapter is specifically written to be extended anonymously, enabling anonymous bean
 * processors that are ordered, and need to only rewrite the method they want to. By default,
 * the ordering of the processor is set to {@link OrderedBean#NEUTRAL_PRECEDENCE}.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:36 PM)
 */
public abstract class BeanProcessorAdapter<E> implements BeanProcessor<E>, OrderedBean {

    private final int order;

    public BeanProcessorAdapter() {
        this(OrderedBean.NEUTRAL_PRECEDENCE);
    }

    public BeanProcessorAdapter(int order) {
        this.order = order;
    }

    @Override
    public E postProcessBeforeRegistration(E bean, String name) throws RegistryException {
        return bean;
    }

    @Override
    public E postProcessBeforeAccess(E bean, String name) throws RegistryException {
        return bean;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
