/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
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

package com.mmnaseri.couteau.context.impl;

import com.mmnaseri.couteau.context.contract.BeanProcessor;
import com.mmnaseri.couteau.context.contract.OrderedBean;
import com.mmnaseri.couteau.context.error.RegistryException;

/**
 * This adapter is specifically written to be extended anonymously, enabling anonymous bean
 * processors that are ordered, and need to only rewrite the method they want to. By default,
 * the ordering of the processor is set to {@link OrderedBean#NEUTRAL_PRECEDENCE}.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
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
