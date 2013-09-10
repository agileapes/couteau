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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class marks an attempt to enhance a class with a number of interfaces.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/10, 18:02)
 */
public class Enhancement {

    private final Class<?> superClass;
    private final Set<Class> interfaces;

    public Enhancement(Class<?> superClass, Class[] interfaces) {
        this.superClass = superClass;
        this.interfaces = new HashSet<Class>(Arrays.asList(interfaces));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enhancement that = (Enhancement) o;
        return interfaces.equals(that.interfaces) && superClass.equals(that.superClass);
    }

    @Override
    public int hashCode() {
        int result = superClass.hashCode();
        result = 31 * result + interfaces.hashCode();
        return result;
    }

}
