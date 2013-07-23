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
 * Accepts all setter methods
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 3:53 PM)
 */
public class SetterMethodFilter implements Filter<Method> {

    @Override
    public boolean accepts(Method item) throws Exception {
        return Modifiers.PUBLIC.matches(item) && !Modifiers.ABSTRACT.matches(item) && !Modifiers.STATIC.matches(item)
                && item.getReturnType().equals(void.class) && item.getParameterTypes().length == 1 && item.getName().matches("set[A-Z].*");
    }

}
