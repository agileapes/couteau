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

package com.mmnaseri.couteau.reflection.beans.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * This class will encapsulate the process of invoking a method on a given object given specific
 * arguments. This is to store invocation information for a later time so that we could reproduce
 * the process of invoking a given method with a given state input readily.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/7/13, 6:00 PM)
 */
public class MethodInvocation {

    /**
     * The method to be invoked
     */
    private final Method method;

    /**
     * The target on which the method is being invoked
     */
    private final Object target;

    /**
     * The arguments for the method invocation.
     */
    private final Object[] arguments;

    public MethodInvocation(Method method, Object target, Object... arguments) {
        this.method = method;
        this.target = target;
        this.arguments = arguments;
    }

    /**
     * Invokes the method on the given target using the given arguments.
     * @return the value returned by the method.
     * @throws InvocationTargetException
     */
    public Object invoke() throws InvocationTargetException {
        method.setAccessible(true);
        try {
            return method.invoke(target, arguments);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodInvocation that = (MethodInvocation) o;
        return Arrays.equals(arguments, that.arguments) && method.equals(that.method) && target.equals(that.target);
    }

    @Override
    public int hashCode() {
        int result = method.hashCode();
        result = 31 * result + target.hashCode();
        result = 31 * result + Arrays.hashCode(arguments);
        return result;
    }

}
