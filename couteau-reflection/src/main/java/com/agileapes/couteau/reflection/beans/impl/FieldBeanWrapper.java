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

package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.reflection.property.ReadPropertyAccessor;
import com.agileapes.couteau.reflection.property.WritePropertyAccessor;
import com.agileapes.couteau.reflection.property.impl.FieldReadPropertyAccessor;
import com.agileapes.couteau.reflection.property.impl.FieldWritePropertyAccessor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import com.agileapes.couteau.reflection.util.assets.MemberModifierFilter;
import com.agileapes.couteau.reflection.util.assets.Modifiers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * This implementation relies on non-static fields of a class to create property metadata. naturally, {@link final}
 * fields are not included in the writable properties.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 2:09 PM)
 */
public class FieldBeanWrapper<E> extends AbstractBeanWrapper<E> {

    /**
     * Instantiates the bean wrapper, taking in the bean to be wrapped
     * @param bean    the been being wrapped
     */
    public FieldBeanWrapper(E bean) {
        super(bean);
    }

    /**
     * This method is expected to return property writers for all properties writable by this bean wrapper.
     * @return the map of property names to property write accessors
     * @throws Exception
     */
    @Override
    protected Map<String, WritePropertyAccessor<?>> getWriteAccessors() throws Exception {
        final HashMap<String, WritePropertyAccessor<?>> map = new HashMap<String, WritePropertyAccessor<?>>();
        ReflectionUtils.withFields(getBeanType()).drop(new MemberModifierFilter(Modifiers.STATIC, Modifiers.FINAL)).each(new Processor<Field>() {
            @Override
            public void process(Field input) throws Exception {
                //noinspection unchecked
                map.put(input.getName(), new FieldWritePropertyAccessor(input, getBean()));
            }
        });
        return map;
    }

    /**
     * This method should present property readers for all available properties in the wrapped bean
     * @return a map of property names to property readers
     * @throws Exception
     */
    @Override
    protected Map<String, ReadPropertyAccessor<?>> getReadAccessors() throws Exception {
        final HashMap<String, ReadPropertyAccessor<?>> map = new HashMap<String, ReadPropertyAccessor<?>>();
        ReflectionUtils.withFields(getBeanType()).drop(new MemberModifierFilter(Modifiers.STATIC)).each(new Processor<Field>() {
            @Override
            public void process(Field input) throws Exception {
                //noinspection unchecked
                map.put(input.getName(), new FieldReadPropertyAccessor(input, getBean()));
            }
        });
        return map;
    }

}
