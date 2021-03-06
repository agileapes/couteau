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
import com.mmnaseri.couteau.reflection.property.impl.MethodReadPropertyAccessor;
import com.mmnaseri.couteau.reflection.property.impl.MethodWritePropertyAccessor;
import com.mmnaseri.couteau.reflection.util.ReflectionUtils;
import com.mmnaseri.couteau.reflection.util.assets.GetterMethodFilter;
import com.mmnaseri.couteau.reflection.util.assets.SetterMethodFilter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Describes the properties of the given class object using its getter and setter methods.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/9/13, 2:15 PM)
 */
public class MethodClassBeanDescriptor<E> extends AbstractClassBeanDescriptor<E> {

    /**
     * Instantiates the bean descriptor while taking in the bean class
     * @param beanClass    the class to be used
     */
    public MethodClassBeanDescriptor(Class<E> beanClass) {
        super(beanClass);
    }

    /**
     * @return a map of property names to property readers
     */
    @Override
    protected Map<String,ReadPropertyAccessor<?>> getReaders() {
        final HashMap<String, ReadPropertyAccessor<?>> map = new HashMap<String, ReadPropertyAccessor<?>>();
        ReflectionUtils.withMethods(getBeanType()).keep(new GetterMethodFilter(isAbstract())).each(new Processor<Method>() {
            @Override
            public void process(Method input) {
                final String propertyName = ReflectionUtils.getPropertyName(input.getName());
                //noinspection unchecked
                map.put(propertyName, new MethodReadPropertyAccessor(input, null));
            }
        });
        return map;
    }

    /**
     * @return a map of property names to property writers
     */
    @Override
    protected Map<String, WritePropertyAccessor<?>> getWriters() {
        final HashMap<String, WritePropertyAccessor<?>> map = new HashMap<String, WritePropertyAccessor<?>>();
        ReflectionUtils.withMethods(getBeanType()).keep(new SetterMethodFilter(isAbstract())).each(new Processor<Method>() {
            @Override
            public void process(Method input) {
                final String propertyName = ReflectionUtils.getPropertyName(input.getName());
                //noinspection unchecked
                map.put(propertyName, new MethodWritePropertyAccessor(input, null));
            }
        });
        return map;
    }

}