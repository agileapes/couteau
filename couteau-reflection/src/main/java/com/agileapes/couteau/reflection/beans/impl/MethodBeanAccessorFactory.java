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

package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.reflection.beans.BeanAccessor;
import com.agileapes.couteau.reflection.beans.BeanAccessorFactory;

/**
 * Returns {@link MethodBeanAccessor} for beans
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 6:53 AM)
 */
public class MethodBeanAccessorFactory implements BeanAccessorFactory {

    /**
     * Returns bean accessors for the given bean
     * @param bean    the bean
     * @param <E>     type of the bean
     * @return the accessor to the bean
     */
    @Override
    public <E> BeanAccessor<E> getBeanAccessor(E bean) {
        return new MethodBeanAccessor<E>(bean);
    }

}
