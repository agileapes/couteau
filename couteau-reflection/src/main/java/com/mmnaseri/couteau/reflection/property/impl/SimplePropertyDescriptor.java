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

package com.mmnaseri.couteau.reflection.property.impl;

import com.mmnaseri.couteau.reflection.property.PropertyDescriptor;

import java.lang.reflect.Type;

/**
 * This is a simple, immutable property descriptor.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:33 AM)
 */
public class SimplePropertyDescriptor implements PropertyDescriptor {

    private final String name;
    private final Class<?> type;
    private final Object value;
    private final Type genericType;

    /**
     * Instantiates the descriptor, defaulting {@link #value} to {@code null}
     * @param name           the name of the property
     * @param type           the type of the property
     * @param genericType    the generic type of the property
     */
    public SimplePropertyDescriptor(String name, Class<?> type, Type genericType) {
        this(name, type, genericType, null);
    }

    /**
     * Instantiates the descriptor
     * @param name           the name of the property
     * @param type           the type of the property
     * @param genericType    the generic type of the property
     * @param value          the value of the property
     */
    public SimplePropertyDescriptor(String name, Class<?> type, Type genericType, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.genericType = genericType;
    }

    /**
     * @return the name of the property
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the type of the property
     */
    @Override
    public Class<?> getType() {
        return type;
    }

    /**
     * @return the generic type of the property
     */
    @Override
    public Type getGenericType() {
        return genericType;
    }

    /**
     * @return the value of the property
     */
    @Override
    public Object getValue() {
        return value;
    }

    /**
     * Determines whether this property has a value of {@code null}
     * @return {@code true} if the value of the property has not been set or if it is set to {@code null}
     */
    @Override
    public boolean isNull() {
        return value == null;
    }

}
