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

package com.agileapes.couteau.enhancer.impl;

import com.agileapes.couteau.enhancer.api.ClassEnhancer;
import com.agileapes.couteau.enhancer.api.NamingPolicy;

import java.util.concurrent.atomic.AtomicLong;

/**
 * This naming policy incorporates the enhancer into the name of the final name of the
 * given class. It also uses an atomic long to suffix the class name so that different
 * namings have different, unique names.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/10, 15:19)
 */
public class DefaultNamingPolicy implements NamingPolicy {

    private final static AtomicLong reference = new AtomicLong(9999L);

    @Override
    public String getClassName(Class<?> originalClass, ClassEnhancer<?> classEnhancer) {
        return originalClass.getCanonicalName() + "$$ENHANCED$$BY$$" + classEnhancer.getClass().getSimpleName() + "$$" + reference.getAndIncrement();
    }

}
