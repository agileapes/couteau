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

package com.mmnaseri.couteau.reflection.beans;

import com.mmnaseri.couteau.reflection.error.NoSuchPropertyException;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * <p>Bean descriptors are general metadata models that hold information about the type of the bean and
 * its properties.</p>
 *
 * <p>Bean descriptors can be used reflectively to manipulate beans easily.</p>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:43 PM)
 */
public interface BeanDescriptor<E> {

    /**
     * @return the type of the bean being described
     */
    Class<E> getBeanType();

    /**
     * @return the names of the properties available to this descriptor
     */
    Set<String> getPropertyNames();

    /**
     * Determines the type of the given property
     * @param propertyName    the name of the property
     * @return this property's type
     * @throws NoSuchPropertyException
     */
    Class<?> getPropertyType(String propertyName) throws NoSuchPropertyException;

    /**
     * Will return the generic type of the property. If the property does not define a generic
     * type, this method's return value will be equivalent to that of {@link #getPropertyType(String)}
     * @param propertyName    the name of the property
     * @return the generic type of the property
     * @throws NoSuchPropertyException
     */
    Type getGenericPropertyType(String propertyName) throws NoSuchPropertyException;

    /**
     * Determines whether this bean has a property with the given name
     * @param propertyName    the name of the property
     * @return {@code true} if the bean has this property, and {@code false} otherwise.
     */
    boolean hasProperty(String propertyName);

}
