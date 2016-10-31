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

import com.mmnaseri.couteau.context.error.InvalidEnumValueError;
import com.mmnaseri.couteau.context.error.InvalidValueTypeError;
import com.mmnaseri.couteau.context.value.ValueReader;

/**
 * This value reader will accept any enums and read their value from a string input
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/5, 15:45)
 */
public class EnumValueReader implements ValueReader {

    /**
     * Accepts all types that are enums
     * @param type    the type to be queried
     * @return {@code true} if the type is an enum
     */
    @Override
    public boolean canRead(Class<?> type) {
        return type.isEnum();
    }

    @Override
    public <E> E read(String text, Class<E> type) {
        if (!canRead(type)) {
            throw new InvalidValueTypeError(type);
        }
        final E[] constants = type.getEnumConstants();
        for (E constant : constants) {
            if (constant.toString().equalsIgnoreCase(text)) {
                return constant;
            }
        }
        throw new InvalidEnumValueError(text, type);
    }

}
