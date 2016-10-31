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
import com.agileapes.couteau.reflection.property.impl.MethodReadPropertyAccessor;
import com.agileapes.couteau.reflection.property.impl.MethodWritePropertyAccessor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import com.agileapes.couteau.reflection.util.assets.GetterMethodFilter;
import com.agileapes.couteau.reflection.util.assets.SetterMethodFilter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Wraps the bean, while exposing its properties through its getter and setter methods
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 2:07 PM)
 */
public class MethodBeanWrapper<E> extends AbstractBeanWrapper<E> {

    /**
     * Instantiates the bean wrapper, taking in the bean to be wrapped
     * @param bean    the been being wrapped
     */
    public MethodBeanWrapper(E bean) {
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
        ReflectionUtils.withMethods(getBeanType()).keep(new SetterMethodFilter()).each(new Processor<Method>() {
            @Override
            public void process(Method input) {
                final String propertyName = ReflectionUtils.getPropertyName(input.getName());
                //noinspection unchecked
                map.put(propertyName, new MethodWritePropertyAccessor(input, getBean()));
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
        final Map<String, ReadPropertyAccessor<?>> map = new HashMap<String, ReadPropertyAccessor<?>>();
        ReflectionUtils.withMethods(getBeanType()).keep(new GetterMethodFilter()).each(new Processor<Method>() {
            @Override
            public void process(Method input) {
                final String propertyName = ReflectionUtils.getPropertyName(input.getName());
                //noinspection unchecked
                map.put(propertyName, new MethodReadPropertyAccessor(input, getBean()));
            }
        });
        return map;
    }

}
