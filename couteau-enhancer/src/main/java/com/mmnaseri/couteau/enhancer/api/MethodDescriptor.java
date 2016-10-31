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

package com.mmnaseri.couteau.enhancer.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * This is a replacement for using {@link Method} descriptors, since this way, the framework
 * can avoid making reflective calls every time the methods are being called
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/10, 17:13)
 */
public interface MethodDescriptor {

    /**
     * @see Method#getDeclaringClass()
     */
    Class<?> getDeclaringClass();

    /**
     * @see Method#getAnnotations()
     */
    Annotation[] getAnnotations();

    /**
     * @see Method#getAnnotation(Class)
     */
    <A extends Annotation> A getAnnotation(Class<A> annotationType);

    /**
     * @see Method#isAnnotationPresent(Class)
     */
    boolean isAnnotationPresent(Class<? extends Annotation> annotationType);

    /**
     * @see Method#getName()
     */
    String getName();

    /**
     * @see Method#getParameterTypes()
     */
    Class[] getParameterTypes();

    /**
     * @see Method#getReturnType()
     */
    Class<?> getReturnType();

    /**
     * @see Method#getExceptionTypes()
     */
    Class<? extends Throwable> getExceptionTypes();

    /**
     * @return the underlying reflection method
     */
    Method getMethod();

}
