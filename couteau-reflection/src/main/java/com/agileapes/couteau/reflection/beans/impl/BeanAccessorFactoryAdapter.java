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
import com.agileapes.couteau.reflection.beans.BeanWrapperFactory;

/**
 * This is a convenience implementation that is designed to naturally convert a {@link BeanWrapperFactory}
 * to a {@link BeanAccessorFactory}, so that the former could be used in the place of the latter, even though
 * cases for such a requirement are extremely unlikely.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/22/13, 12:05 PM)
 */
public class BeanAccessorFactoryAdapter implements BeanAccessorFactory {

    private final BeanWrapperFactory wrapperFactory;

    public BeanAccessorFactoryAdapter(BeanWrapperFactory wrapperFactory) {
        this.wrapperFactory = wrapperFactory;
    }

    @Override
    public <E> BeanAccessor<E> getBeanAccessor(E bean) {
        return wrapperFactory.getBeanWrapper(bean);
    }

}
