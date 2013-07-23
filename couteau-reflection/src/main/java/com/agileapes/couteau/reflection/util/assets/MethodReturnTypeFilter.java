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

package com.agileapes.couteau.reflection.util.assets;

import com.agileapes.couteau.basics.api.Filter;

import java.lang.reflect.Method;

/**
 * Filters methods by their return types
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 5:28 PM)
 */
public class MethodReturnTypeFilter implements Filter<Method> {

    private final Class<?> returnType;

    public MethodReturnTypeFilter(Class<?> returnType) {
        this.returnType = returnType;
    }

    @Override
    public boolean accepts(Method item) throws Exception {
        return returnType.isAssignableFrom(item.getReturnType());
    }

}
