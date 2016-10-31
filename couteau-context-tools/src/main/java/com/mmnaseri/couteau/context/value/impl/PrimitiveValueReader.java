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
import com.mmnaseri.couteau.context.value.ValueReader;
import com.agileapes.couteau.reflection.util.ReflectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * This is a value reader capable of reading values for all the primitive value type defined
 * in Java.
 *
 * @see #CLASSES See the types this reader understand
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/30, 13:57)
 */
public class PrimitiveValueReader implements ValueReader {

    @SuppressWarnings("unchecked")
    private final static List<? extends Class> CLASSES = Arrays.asList(int.class, long.class, short.class, boolean.class, char.class,
            float.class, double.class, Integer.class, Long.class, Short.class, Boolean.class,
            Character.class, Float.class, Double.class, String.class);

    /**
     * @param type    the type to be queried
     * @return {@code true} if it is one of the types in {@link #CLASSES}
     */
    @Override
    public boolean canRead(Class<?> type) {
        return CLASSES.contains(type);
    }

    @SuppressWarnings({"unchecked", "UnnecessaryUnboxing"})
    @Override
    public <E> E read(String text, Class<E> type) {
        if (!canRead(type)) {
            throw new InvalidValueTypeError(type);
        }
        if (Number.class.isAssignableFrom(ReflectionUtils.mapType(type))) {
            try {
                if (type.equals(int.class) || type.equals(Integer.class)) {
                    return (E) (Integer) Integer.parseInt(text);
                } else if (type.equals(long.class) || type.equals(Long.class)) {
                    return (E) (Long) Long.parseLong(text);
                } else if (type.equals(short.class) || type.equals(Short.class)) {
                    return (E) (Short) Short.parseShort(text);
                } else if (type.equals(float.class) || type.equals(Float.class)) {
                    return (E) (Float) Float.parseFloat(text);
                } else if (type.equals(double.class) || type.equals(Double.class)) {
                    return (E) (Double) Double.parseDouble(text);
                } else {
                    throw new InvalidValueTypeError(type);
                }
            } catch (NumberFormatException e) {
                throw new InvalidInputValueError(text, type, e);
            }
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            if (Arrays.asList("1", "yes", "true", "on").contains(text.toLowerCase())) {
                return (E) ((Boolean) true);
            } else if (Arrays.asList("0", "no", "false", "off").contains(text.toLowerCase())) {
                return (E) ((Boolean) false);
            } else {
                throw new InvalidInputValueError(text, type);
            }
        } else if (type.equals(char.class) || type.equals(Character.class)) {
            if (text.length() != 1) {
                throw new IllegalArgumentException(text + " is not a valid character");
            }
            return (E) ((Character) text.charAt(0));
        } else {
            return (E) text;
        }
    }

}
