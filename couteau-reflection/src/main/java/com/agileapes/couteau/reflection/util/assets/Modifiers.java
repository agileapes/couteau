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

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * Encapsulates all modifiers currently supported by Java
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 2:23 PM)
 */
public enum Modifiers {

    PUBLIC(new Filter<Integer>() {
        @Override
        public boolean accepts(Integer item) {
            return Modifier.isPublic(item);
        }
    }),
    PRIVATE(new Filter<Integer>() {
        @Override
        public boolean accepts(Integer item) {
            return Modifier.isPrivate(item);
        }
    }),
    PROTECTED(new Filter<Integer>() {
        @Override
        public boolean accepts(Integer item) {
            return Modifier.isProtected(item);
        }
    }),
    STATIC(new Filter<Integer>() {
        @Override
        public boolean accepts(Integer item) {
            return Modifier.isStatic(item);
        }
    }),
    FINAL(new Filter<Integer>() {
        @Override
        public boolean accepts(Integer item) {
            return Modifier.isFinal(item);
        }
    }),
    SYNCHRONIZED(new Filter<Integer>() {
        @Override
        public boolean accepts(Integer item) {
            return Modifier.isSynchronized(item);
        }
    }),
    VOLATILE(new Filter<Integer>() {
        @Override
        public boolean accepts(Integer item) {
            return Modifier.isVolatile(item);
        }
    }),
    TRANSIENT(new Filter<Integer>() {
        @Override
        public boolean accepts(Integer item) {
            return Modifier.isTransient(item);
        }
    }),
    NATIVE(new Filter<Integer>() {
        @Override
        public boolean accepts(Integer item) {
            return Modifier.isNative(item);
        }
    }),
    INTERFACE(new Filter<Integer>() {
        @Override
        public boolean accepts(Integer item) {
            return Modifier.isInterface(item);
        }
    }),
    ABSTRACT(new Filter<Integer>() {
        @Override
        public boolean accepts(Integer item) {
            return Modifier.isAbstract(item);
        }
    }),
    STRICT(new Filter<Integer>() {
        @Override
        public boolean accepts(Integer item) {
            return Modifier.isStrict(item);
        }
    })
    ;

    private final Filter<Integer> modifierFilter;

    private Modifiers(Filter<Integer> modifierFilter) {
        this.modifierFilter = modifierFilter;
    }

    public boolean matches(Member member) {
        return matches(member.getModifiers());
    }

    public boolean matches(int modifier) {
        try {
            return modifierFilter.accepts(modifier);
        } catch (Exception e) {
            return false;
        }
    }

}
