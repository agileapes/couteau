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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Filters items by their annotations
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 2:21 PM)
 */
public class AnnotatedElementFilter implements Filter<AnnotatedElement> {

    private final Class<? extends Annotation>[] annotations;

    public AnnotatedElementFilter(Class<? extends Annotation>... annotations) {
        this.annotations = annotations;
    }

    @Override
    public boolean accepts(AnnotatedElement item) {
        for (Class<? extends Annotation> annotation : annotations) {
            if (item.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }
}
