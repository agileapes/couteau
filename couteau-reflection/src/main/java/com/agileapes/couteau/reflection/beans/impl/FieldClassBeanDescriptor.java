/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agileapes.couteau.reflection.beans.impl;

import com.mmnaseri.couteau.basics.api.Processor;
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
 * Describes the properties of a given class using its fields.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 2:45 PM)
 */
public class FieldClassBeanDescriptor<E> extends AbstractClassBeanDescriptor<E> {

    /**
     * Instantiates the bean descriptor while taking in the bean class
     * @param beanClass    the class to be used
     */
    public FieldClassBeanDescriptor(Class<E> beanClass) {
        super(beanClass);
    }

    /**
     * @return a map of property names to property readers
     * @throws Exception
     */
    @Override
    protected Map<String, ReadPropertyAccessor<?>> getReaders() {
        final HashMap<String, ReadPropertyAccessor<?>> map = new HashMap<String, ReadPropertyAccessor<?>>();
        ReflectionUtils.withFields(getBeanType()).drop(new MemberModifierFilter(Modifiers.STATIC)).each(new Processor<Field>() {
            @Override
            public void process(Field input) {
                //noinspection unchecked
                map.put(input.getName(), new FieldReadPropertyAccessor(input, null));
            }
        });
        return map;
    }

    /**
     * @return a map of property names to property writers
     * @throws Exception
     */
    @Override
    protected Map<String, WritePropertyAccessor<?>> getWriters() {
        final HashMap<String, WritePropertyAccessor<?>> map = new HashMap<String, WritePropertyAccessor<?>>();
        ReflectionUtils.withFields(getBeanType()).drop(new MemberModifierFilter(Modifiers.STATIC, Modifiers.FINAL)).each(new Processor<Field>() {
            @Override
            public void process(Field input) {
                //noinspection unchecked
                map.put(input.getName(), new FieldWritePropertyAccessor(input, null));
            }
        });
        return map;
    }

}
