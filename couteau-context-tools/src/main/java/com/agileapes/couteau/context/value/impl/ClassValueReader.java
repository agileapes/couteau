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

package com.agileapes.couteau.context.value.impl;

import com.agileapes.couteau.context.error.InvalidInputValueError;
import com.agileapes.couteau.context.error.InvalidValueTypeError;
import com.agileapes.couteau.context.error.ValueReaderError;
import com.agileapes.couteau.context.util.ClassUtils;
import com.agileapes.couteau.context.value.ValueReader;

/**
 * This value reader will take as input the fully qualified name of a class within the classpath
 * as seen by the {@link #classLoader} and return the class object
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/4, 19:08)
 */
public class ClassValueReader implements ValueReader {

    private ClassLoader classLoader = getClass().getClassLoader();

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public boolean canRead(Class<?> type) {
        return Class.class.equals(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E read(String text, Class<E> type) throws ValueReaderError{
        if (!canRead(type)) {
            throw new InvalidValueTypeError(type);
        }
        try {
            return (E) ClassUtils.forName(text, classLoader);
        } catch (ClassNotFoundException e) {
            throw new InvalidInputValueError(text, type, e);
        }
    }
}
