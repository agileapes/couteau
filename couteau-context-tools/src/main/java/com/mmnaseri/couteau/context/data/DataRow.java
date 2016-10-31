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

package com.mmnaseri.couteau.context.data;

import java.lang.reflect.Type;

/**
 * This interface represents a row of data
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (14/6/21 AD, 17:01)
 */
public interface DataRow<P> extends Iterable<DataProperty<P>> {

    /**
     * The value of a property in its string form
     * @param property    the property key
     * @return the property value or {@code null} if it has not been set
     */
    String getValue(P property);

    /**
     * The value of a property in the desired type
     * @param property      the property key
     * @param targetType    the type of the property
     * @param <E>           the type parameter of the property
     * @return the property value or {@code null} if it has not been set
     */
    <E> E getValue(P property, Class<E> targetType);

    /**
     * Reads the value of the target type, allowing for generic values to be read
     * @param property      the property key
     * @param targetType    the type of the property
     * @return the property or {@code null} if it has not been set
     */
    Object getValue(P property, Type targetType);

    /**
     * @return the data sheet owning this row
     */
    DataSheet<P> getDataSheet();

}
