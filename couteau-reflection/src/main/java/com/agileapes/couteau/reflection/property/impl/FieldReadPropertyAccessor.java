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

package com.agileapes.couteau.reflection.property.impl;

import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.property.ReadPropertyAccessor;

import java.lang.reflect.Field;

/**
 * This class allows for reading of property values through declared fields
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:40 PM)
 */
public class FieldReadPropertyAccessor<E> extends AbstractFieldPropertyAccessor<E> implements ReadPropertyAccessor<E> {

    /**
     * Instantiates the accessor, while wrapping the field and taking in the invocation target
     * @param field     the field
     * @param target    the target
     */
    public FieldReadPropertyAccessor(Field field, Object target) {
        super(field, target);
    }

    /**
     * Reads and returns the current value of the property
     * @return the value of the property
     * @throws PropertyAccessException
     */
    @Override
    public E getPropertyValue() throws PropertyAccessException {
        try {
            //noinspection unchecked
            return (E) get();
        } catch (Exception e) {
            throw new PropertyAccessException(getTargetType(), getName(), e);
        }
    }

}
