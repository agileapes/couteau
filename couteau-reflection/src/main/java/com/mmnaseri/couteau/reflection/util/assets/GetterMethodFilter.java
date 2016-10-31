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

package com.mmnaseri.couteau.reflection.util.assets;

import com.mmnaseri.couteau.basics.api.Filter;

import java.lang.reflect.Method;

/**
 * Accepts all getter methods
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/6/13, 3:44 PM)
 */
public class GetterMethodFilter implements Filter<Method> {

    private final boolean acceptAbstract;

    public GetterMethodFilter() {
        this(false);
    }

    public GetterMethodFilter(boolean acceptAbstract) {
        this.acceptAbstract = acceptAbstract;
    }

    @Override
    public boolean accepts(Method item) {
        return Modifiers.PUBLIC.matches(item) && (acceptAbstract || !Modifiers.ABSTRACT.matches(item)) && !Modifiers.STATIC.matches(item)
                && !item.getReturnType().equals(void.class) && item.getParameterTypes().length == 0 &&
                (item.getName().matches("get[A-Z].*") || (item.getName().matches("is[A-Z].*") && (item.getReturnType().equals(boolean.class) || item.getReturnType().equals(Boolean.class))));
    }

}
