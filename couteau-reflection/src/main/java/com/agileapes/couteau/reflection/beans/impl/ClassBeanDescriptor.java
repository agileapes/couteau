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

import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.reflection.property.ReadPropertyAccessor;
import com.agileapes.couteau.reflection.property.WritePropertyAccessor;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>This is a class bean descriptor akin to {@link MethodClassBeanDescriptor} and its closely related
 * sister {@link FieldClassBeanDescriptor}. In fact, this class is designed to combine the functionalities
 * of the two classes into one.</p>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/22/13, 3:35 PM)
 */
public class ClassBeanDescriptor<E> extends AbstractClassBeanDescriptor<E> {

    /**
     * This enum will allow you to select the lookup method used for property accessors.
     */
    public static enum Lookup {
        /**
         * Maps to {@link MethodClassBeanDescriptor}
         */
        METHOD,
        /**
         * Maps to {@link FieldClassBeanDescriptor}
         */
        FIELD,
        /**
         * Combines both {@link MethodClassBeanDescriptor} and {@link FieldClassBeanDescriptor}, while
         * allowing property accessors with a method accessor overriding those with a field accessor.
         * This way, if a property is both accessible via a field and via an accessor method, the accessor
         * method is given precedence.
         */
        BOTH
    }

    private FieldClassBeanDescriptor<E> fieldDescriptor;
    private MethodClassBeanDescriptor<E> methodDescriptor;
    
    /**
     * Instantiates the bean descriptor while taking in the bean class
     *
     * @param beanClass the class to be used
     */
    public ClassBeanDescriptor(final Class<E> beanClass, final Lookup lookup) {
        super(beanClass, new Processor<AbstractClassBeanDescriptor<E>>() {
            @Override
            public void process(AbstractClassBeanDescriptor<E> input) {
                final ClassBeanDescriptor descriptor = (ClassBeanDescriptor) input;
                if (!Lookup.METHOD.equals(lookup)) {
                    descriptor.fieldDescriptor = new FieldClassBeanDescriptor<E>(beanClass);
                }
                if (!Lookup.FIELD.equals(lookup)) {
                    descriptor.methodDescriptor = new MethodClassBeanDescriptor<E>(beanClass);
                }
            }
        });
    }

    @Override
    protected Map<String, ReadPropertyAccessor<?>> getReaders() {
        final HashMap<String, ReadPropertyAccessor<?>> map = new HashMap<String, ReadPropertyAccessor<?>>();
        if (fieldDescriptor != null) {
            map.putAll(fieldDescriptor.getReaders());
        }
        if (methodDescriptor != null) {
            map.putAll(methodDescriptor.getReaders());
        }
        return map;
    }

    @Override
    protected Map<String, WritePropertyAccessor<?>> getWriters() {
        final HashMap<String, WritePropertyAccessor<?>> map = new HashMap<String, WritePropertyAccessor<?>>();
        if (fieldDescriptor != null) {
            map.putAll(fieldDescriptor.getWriters());
        }
        if (methodDescriptor != null) {
            map.putAll(methodDescriptor.getWriters());
        }
        return map;
    }

}
