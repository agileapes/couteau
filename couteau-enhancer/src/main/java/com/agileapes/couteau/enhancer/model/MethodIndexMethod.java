/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.couteau.enhancer.model;

import com.agileapes.couteau.enhancer.api.MethodDescriptor;
import com.agileapes.couteau.enhancer.impl.ImmutableMethodDescriptor;
import com.agileapes.couteau.freemarker.api.Invokable;
import com.agileapes.couteau.freemarker.model.TypedMethodModel;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * This method model will associate a single, unique number to each method
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/10, 18:40)
 */
public class MethodIndexMethod extends TypedMethodModel {

    private final Map<MethodDescriptor, Integer> indices = new HashMap<MethodDescriptor, Integer>();

    @Invokable
    public int getIndex(Method method) {
        final ImmutableMethodDescriptor descriptor = new ImmutableMethodDescriptor(method.getDeclaringClass(), method.getReturnType(), method.getName(), method.getParameterTypes(), method.getAnnotations());
        if (!indices.containsKey(descriptor)) {
            indices.put(descriptor, indices.size());
        }
        return indices.get(descriptor);
    }

}
