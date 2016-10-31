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

import com.mmnaseri.couteau.reflection.beans.BeanInitializer;
import com.mmnaseri.couteau.reflection.error.BeanInstantiationException;
import com.mmnaseri.couteau.reflection.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * This implementation of {@link BeanInitializer} will allow for easy bean construction through
 * declared constructors.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/4/13, 6:55 PM)
 */
public class ConstructorBeanInitializer implements BeanInitializer {

    private final boolean allowAccessible;

    public ConstructorBeanInitializer() {
        this(false);
    }

    public ConstructorBeanInitializer(boolean allowAccessible) {
        this.allowAccessible = allowAccessible;
    }

    /**
     * <p>This method will initialize a bean of the given type using the specified initialization
     * arguments.</p>
     *
     * <p>Any errors occurring during this process will be reflected as a BeanInstantiationException
     * to simplify handing exceptions for bean initialization.</p>
     * @param type             the type of the bean
     * @param argumentTypes    the types of the arguments being passed
     * @param arguments        the arguments used for initialization of the bean
     * @param <E>              the type of the bean
     * @return an instance of the bean in question
     * @throws BeanInstantiationException
     */
    @Override
    public <E> E initialize(Class<E> type, Class[] argumentTypes, Object... arguments) throws BeanInstantiationException {
        if (argumentTypes == null) {
            throw new BeanInstantiationException("Argument types cannot be null");
        }
        if (arguments == null) {
            throw new BeanInstantiationException("Arguments cannot be null");
        }
        if (argumentTypes.length != arguments.length) {
            throw new BeanInstantiationException("Argument types must match arguments");
        }
        for (int i = 0; i < argumentTypes.length; i++) {
            final Class argumentType = ReflectionUtils.mapType(argumentTypes[i]);
            final Object argument = arguments[i];
            if (!argumentType.isInstance(argument) && ((argumentType.isPrimitive() || argument != null))) {
                throw new BeanInstantiationException("Arguments do not match the provided types");
            }
        }
        if (Modifier.isAbstract(type.getModifiers())) {
            throw new BeanInstantiationException("Cannot instantiate abstract class: " + type.getCanonicalName());
        }
        if (type.isInterface()) {
            throw new BeanInstantiationException("Cannot instantiate interface: " + type.getCanonicalName());
        }
        if (!Modifier.isPublic(type.getModifiers())) {
            if (!allowAccessible) {
                throw new BeanInstantiationException("Cannot instantiate a class unless it is public: " + type.getCanonicalName());
            }
        }
        final Constructor<?>[] constructors = type.getDeclaredConstructors();
        final Set<Constructor<?>> candidates = new HashSet<Constructor<?>>();
        for (Constructor<?> constructor : constructors) {
            if ((allowAccessible || Modifier.isPublic(constructor.getModifiers())) && constructor.getParameterTypes().length == arguments.length) {
                boolean matches = true;
                for (int i = 0; i < arguments.length; i ++) {
                    if (!constructor.getParameterTypes()[i].isAssignableFrom(argumentTypes[i])) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    candidates.add(constructor);
                }
            }
        }
        if (candidates.isEmpty()) {
            throw new BeanInstantiationException("No matching constructor found for specified arguments: " + type.getCanonicalName());
        }
        if (candidates.size() > 1) {
            throw new BeanInstantiationException("Ambiguous call to constructors for " + type.getCanonicalName());
        }
        final Constructor<?> constructor = candidates.iterator().next();
        constructor.setAccessible(true);
        final Object instance;
        try {
            instance = constructor.newInstance(arguments);
        } catch (IllegalArgumentException e) {
            throw new BeanInstantiationException("Invalid arguments passed to constructor", e);
        } catch (InstantiationException e) {
            throw new BeanInstantiationException("Failed to instantiate bean of type " + type.getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            throw new BeanInstantiationException("Access denied to constructor: " + type.getCanonicalName(), e);
        } catch (InvocationTargetException e) {
            throw new BeanInstantiationException("Construction failed due to errors inside the constructor for type " + type.getCanonicalName(), e);
        }
        return type.cast(instance);
    }

}
