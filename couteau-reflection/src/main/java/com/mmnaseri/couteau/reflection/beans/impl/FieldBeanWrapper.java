/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
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

package com.mmnaseri.couteau.reflection.beans.impl;

import com.mmnaseri.couteau.basics.api.Processor;
import com.mmnaseri.couteau.reflection.property.ReadPropertyAccessor;
import com.mmnaseri.couteau.reflection.property.WritePropertyAccessor;
import com.mmnaseri.couteau.reflection.property.impl.FieldReadPropertyAccessor;
import com.mmnaseri.couteau.reflection.property.impl.FieldWritePropertyAccessor;
import com.mmnaseri.couteau.reflection.util.ReflectionUtils;
import com.mmnaseri.couteau.reflection.util.assets.MemberModifierFilter;
import com.mmnaseri.couteau.reflection.util.assets.Modifiers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * This implementation relies on non-static fields of a class to create property metadata. naturally, {@link final}
 * fields are not included in the writable properties.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
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
    protected Map<String, WritePropertyAccessor<?>> getWriteAccessors() {
        final HashMap<String, WritePropertyAccessor<?>> map = new HashMap<String, WritePropertyAccessor<?>>();
        ReflectionUtils.withFields(getBeanType()).drop(new MemberModifierFilter(Modifiers.STATIC, Modifiers.FINAL)).each(new Processor<Field>() {
            @Override
            public void process(Field input) {
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
    protected Map<String, ReadPropertyAccessor<?>> getReadAccessors() {
        final HashMap<String, ReadPropertyAccessor<?>> map = new HashMap<String, ReadPropertyAccessor<?>>();
        ReflectionUtils.withFields(getBeanType()).drop(new MemberModifierFilter(Modifiers.STATIC)).each(new Processor<Field>() {
            @Override
            public void process(Field input) {
                //noinspection unchecked
                map.put(input.getName(), new FieldReadPropertyAccessor(input, getBean()));
            }
        });
        return map;
    }

}
