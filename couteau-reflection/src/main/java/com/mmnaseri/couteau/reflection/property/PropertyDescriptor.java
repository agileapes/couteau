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

package com.mmnaseri.couteau.reflection.property;

import java.lang.reflect.Type;

/**
 * This interface will help describe a property. All of its signature features will be included in this
 * interface.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/13/13, 7:32 AM)
 */
public interface PropertyDescriptor {

    /**
     * @return the name of the property
     */
    String getName();

    /**
     * @return the type of the property
     */
    Class<?> getType();

    /**
     * @return the generic type of the property
     */
    Type getGenericType();

    /**
     * @return the value of the property
     */
    Object getValue();

    /**
     * Determines whether this property has a value of {@code null}
     * @return {@code true} if the value of the property has not been set or if it is set to {@code null}
     */
    boolean isNull();

}
