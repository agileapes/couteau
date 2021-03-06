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

package com.mmnaseri.couteau.reflection.property.impl;

import com.mmnaseri.couteau.reflection.error.PropertyAccessException;
import com.mmnaseri.couteau.reflection.property.ReadPropertyAccessor;

import java.lang.reflect.Method;

/**
 * This class reads the value of a property through the associated getter method
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/9/13, 12:29 PM)
 */
public class MethodReadPropertyAccessor<E> extends AbstractMethodPropertyAccessor<E> implements ReadPropertyAccessor<E> {

    /**
     * Instantiates the accessor through the method
     * @param method    the method
     * @param target    the object being targeted
     */
    public MethodReadPropertyAccessor(Method method, Object target) {
        super(method, target);
    }

    /**
     * Reads and returns the current value of the property
     * @return the value of the property
     * @throws PropertyAccessException
     */
    @Override
    public E getPropertyValue() throws PropertyAccessException {
        try {
            //noinspection unchecked
            return (E) invoke();
        } catch (Exception e) {
            throw new PropertyAccessException(getTargetType(), getName(), e);
        }
    }

    /**
     * @return the type of the property being wrapped.
     */
    @Override
    public Class<E> getPropertyType() {
        //noinspection unchecked
        return (Class<E>) getMethod().getReturnType();
    }

}
