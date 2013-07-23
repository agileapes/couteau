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

import com.agileapes.couteau.context.contract.Context;
import com.agileapes.couteau.context.error.InvalidBeanNameException;
import com.agileapes.couteau.context.error.RegistryException;

import static com.agileapes.couteau.context.contract.OrderedBean.HIGHEST_PRECEDENCE;

/**
 * This is a thread-safe context that requires items to be named after the canonical names
 * of their classes. This ensures that singleton objects within the context are unique by
 * their types as well.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:35 PM)
 */
public abstract class AbstractTypeSpecificContext<E> extends AbstractThreadSafeContext<E> {

    public AbstractTypeSpecificContext() {
        addBeanProcessor(new BeanProcessorAdapter<E>(HIGHEST_PRECEDENCE) {
            @Override
            public E postProcessBeforeRegistration(E bean, String name) throws RegistryException {
                if (name == null || !name.equals(bean.getClass().getCanonicalName())) {
                    throw new InvalidBeanNameException(bean.getClass().getCanonicalName(), name);
                }
                return bean;
            }
        });
    }

    @Override
    public Context<E> register(E item) throws RegistryException {
        register(item.getClass().getCanonicalName(), item);
        return this;
    }

}
