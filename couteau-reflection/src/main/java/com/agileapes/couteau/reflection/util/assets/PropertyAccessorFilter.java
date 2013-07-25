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
 * Accepts all setter and getter methods for a given property name
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 5:30 PM)
 */
public class PropertyAccessorFilter implements Filter<Method> {

    private final String[] properties;
    private final GetterMethodFilter getterMethodFilter;
    private final SetterMethodFilter setterMethodFilter;

    public PropertyAccessorFilter(String... properties) {
        this.properties = new String[properties.length];
        for (int i = 0; i < properties.length; i ++) {
            this.properties[i] = properties[i].substring(0, 1).toUpperCase().concat(properties[i].substring(1));
        }
        getterMethodFilter = new GetterMethodFilter();
        setterMethodFilter = new SetterMethodFilter();
    }

    @Override
    public boolean accepts(Method item) {
        if (item.getName().length() < 3) {
            return false;
        }
        final String propertyName = item.getName().matches("is.*") ? item.getName().substring(2) : item.getName().substring(3);
        for (String property : properties) {
            if ((getterMethodFilter.accepts(item) || setterMethodFilter.accepts(item)) && propertyName.equals(property)) {
                return true;
            }
        }
        return false;
    }

}
