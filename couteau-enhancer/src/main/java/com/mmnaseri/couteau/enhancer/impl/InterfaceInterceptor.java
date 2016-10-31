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
import com.mmnaseri.couteau.enhancer.api.MethodInterceptor;
import com.mmnaseri.couteau.enhancer.api.MethodProxy;
import com.mmnaseri.couteau.enhancer.error.BeanInitializationError;
import com.mmnaseri.couteau.reflection.beans.BeanInitializer;
import com.mmnaseri.couteau.reflection.beans.impl.ConstructorBeanInitializer;
import com.mmnaseri.couteau.reflection.error.BeanInstantiationException;
import com.mmnaseri.couteau.reflection.util.assets.MemberNameFilter;
import com.mmnaseri.couteau.reflection.util.assets.MethodArgumentsFilter;
import com.mmnaseri.couteau.reflection.util.assets.MethodReturnTypeFilter;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.mmnaseri.couteau.reflection.util.ReflectionUtils.withMethods;

/**
 * This interceptor allows for dynamic assignment of interface implementation as delegates
 * and also delegates calls to all interfaces implemented by the callback itself.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/10, 22:11)
 */
public abstract class InterfaceInterceptor implements MethodInterceptor {

    private final Map<Class, Object> interfaces = new ConcurrentHashMap<Class, Object>();
    private final BeanInitializer beanInitializer = new ConstructorBeanInitializer();

    public InterfaceInterceptor() {
        for (Class superType : getClass().getInterfaces()) {
            interfaces.put(superType, this);
        }
    }

    protected abstract Object call(MethodDescriptor methodDescriptor, Object target, Object[] arguments, MethodProxy methodProxy) throws Throwable;

    public Class[] getInterfaces() {
        return interfaces.keySet().toArray(new Class[interfaces.size()]);
    }

    @Override
    public Object intercept(MethodDescriptor methodDescriptor, Object target, Object[] arguments, MethodProxy methodProxy) throws Throwable {
        if (interfaces.containsKey(methodDescriptor.getDeclaringClass())) {
            Object targetObject = interfaces.get(methodDescriptor.getDeclaringClass());
            final Class<?> searchTarget;
            if (targetObject.equals(this)) {
                searchTarget = getClass();
            } else {
                searchTarget = targetObject.getClass();
            }
            final Method method = withMethods(searchTarget)
                    .keep(new MethodReturnTypeFilter(methodDescriptor.getReturnType()))
                    .keep(new MemberNameFilter(methodDescriptor.getName()))
                    .keep(new MethodArgumentsFilter(methodDescriptor.getParameterTypes()))
                    .first();
            if (method != null) {
                return method.invoke(targetObject, arguments);
            }
        }
        return call(methodDescriptor, target, arguments, methodProxy);
    }

    public void addInterface(Class<?> contract, Class<?> implementation) {
        final Object concrete;
        try {
            concrete = beanInitializer.initialize(implementation, new Class[0]);
        } catch (BeanInstantiationException e) {
            throw new BeanInitializationError(implementation, e);
        }
        interfaces.put(contract, concrete);
    }

}
