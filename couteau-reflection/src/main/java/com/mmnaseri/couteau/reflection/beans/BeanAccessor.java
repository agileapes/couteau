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
import com.mmnaseri.couteau.reflection.error.PropertyAccessException;

/**
 * This interface will add to the capabilities  of {@link BeanDescriptor} by enabling interface
 * methods that will give access to properties in the underlying bean. The bean accessor is basically
 * a gate to read-only access to a bean's properties.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/9/13, 12:45 PM)
 */
public interface BeanAccessor<E> extends BeanDescriptor<E> {

    /**
     * Reads the value of the given property
     * @param propertyName    the name of the property
     * @return the value of the property
     * @throws NoSuchPropertyException if no property exists with the given name
     * @throws PropertyAccessException if there was a problem reading the value of the property
     */
    Object getPropertyValue(String propertyName) throws NoSuchPropertyException, PropertyAccessException;

    /**
     * Reads the value of the given property of the given type
     * @param propertyName      the name of the property
     * @param propertyType      the type of the property
     * @return the value of the property
     * @throws NoSuchPropertyException if no property exists of the given name-type pair
     * @throws PropertyAccessException if there was a problem reading the value of the property
     */
    <P> P getPropertyValue(String propertyName, Class<P> propertyType) throws NoSuchPropertyException, PropertyAccessException;

    /**
     * Exposes the underlying bean
     * @return the bean
     */
    E getBean();

}
