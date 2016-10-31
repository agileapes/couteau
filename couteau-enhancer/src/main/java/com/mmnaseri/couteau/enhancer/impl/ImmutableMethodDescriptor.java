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

package com.mmnaseri.couteau.enhancer.impl;

import com.mmnaseri.couteau.enhancer.api.MethodDescriptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * This is an immutable method descriptor
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/10, 17:16)
 */
public class ImmutableMethodDescriptor implements MethodDescriptor {

    private final Class<?> declaringClass;
    private final Class<?> returnType;
    private final String name;
    private final Class[] parameterTypes;
    private final Annotation[] annotations;

    public ImmutableMethodDescriptor(Class<?> declaringClass, Class<?> returnType, String name, Class[] parameterTypes, Annotation[] annotations) {
        this.declaringClass = declaringClass;
        this.returnType = returnType;
        this.name = name;
        this.parameterTypes = parameterTypes;
        this.annotations = annotations;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    @Override
    public Annotation[] getAnnotations() {
        return annotations;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        for (Annotation annotation : annotations) {
            if (annotationType.isInstance(annotation)) {
                //noinspection unchecked
                return (A) annotation;
            }
        }
        return null;
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return getAnnotation(annotationType) != null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public Class<?> getReturnType() {
        return returnType;
    }

    @Override
    public Class<? extends Throwable> getExceptionTypes() {
        return null;
    }

    @Override
    public Method getMethod() {
        try {
            return declaringClass.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableMethodDescriptor that = (ImmutableMethodDescriptor) o;
        return name.equals(that.name) && Arrays.equals(parameterTypes, that.parameterTypes);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + Arrays.hashCode(parameterTypes);
        return result;
    }

    @Override
    public String toString() {
        return returnType.getCanonicalName() + " " + declaringClass.getCanonicalName() + "." + name + "(" + Arrays.toString(parameterTypes) + ")";
    }

}
