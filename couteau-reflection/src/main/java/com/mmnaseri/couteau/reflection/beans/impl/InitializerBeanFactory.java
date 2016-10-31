/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
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

package com.mmnaseri.couteau.reflection.beans.impl;

import com.mmnaseri.couteau.reflection.beans.BeanFactory;
import com.mmnaseri.couteau.reflection.beans.BeanInitializer;
import com.mmnaseri.couteau.reflection.error.BeanFactoryException;
import com.mmnaseri.couteau.reflection.error.BeanInstantiationException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/18 AD, 14:31)
 */
public class InitializerBeanFactory implements BeanFactory {

    private final BeanInitializer beanInitializer;

    public InitializerBeanFactory() {
        this(new ConstructorBeanInitializer());
    }

    public InitializerBeanFactory(BeanInitializer beanInitializer) {
        this.beanInitializer = beanInitializer;
    }

    @Override
    public <T> T getBean(Class<? extends T> beanType) throws BeanFactoryException {
        try {
            return beanInitializer.initialize(beanType, new Class[0]);
        } catch (BeanInstantiationException e) {
            throw new BeanFactoryException("Could not instantiate bean of type " + beanType.getCanonicalName(), e);
        }
    }

}
