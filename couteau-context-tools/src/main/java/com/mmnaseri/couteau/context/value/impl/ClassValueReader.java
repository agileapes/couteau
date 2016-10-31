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

package com.mmnaseri.couteau.context.value.impl;

import com.mmnaseri.couteau.context.error.InvalidInputValueError;
import com.mmnaseri.couteau.context.error.InvalidValueTypeError;
import com.mmnaseri.couteau.context.error.ValueReaderError;
import com.mmnaseri.couteau.context.value.ValueReader;
import com.mmnaseri.couteau.reflection.util.ClassUtils;

/**
 * This value reader will take as input the fully qualified name of a class within the classpath
 * as seen by the {@link #classLoader} and return the class object
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/6/4, 19:08)
 */
public class ClassValueReader implements ValueReader {

    private ClassLoader classLoader = getClass().getClassLoader();

    /**
     * Sets the class loader through which specified classes will be loaded dynamically
     * @param classLoader    the class loader to be used
     */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * @param type    the type to be queried
     * @return {@code true} if the type is a descendant of {@link Class}
     */
    @Override
    public boolean canRead(Class<?> type) {
        return Class.class.equals(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E read(String text, Class<E> type) throws ValueReaderError {
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
