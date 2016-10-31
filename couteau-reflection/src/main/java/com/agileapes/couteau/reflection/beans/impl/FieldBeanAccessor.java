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
import com.agileapes.couteau.reflection.property.impl.FieldReadPropertyAccessor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import com.agileapes.couteau.reflection.util.assets.MemberModifierFilter;
import com.agileapes.couteau.reflection.util.assets.Modifiers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * This implementation relies on non-static fields of a bean to generate property accessor metadata.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 1:43 PM)
 */
public class FieldBeanAccessor<E> extends AbstractBeanAccessor<E> {

    /**
     * Instantiates the accessor, while taking the bean to be wrapped
     * @param bean    the bean to be wrapped. This can be later accessed through {@link #getBean()}
     */
    public FieldBeanAccessor(E bean) {
        super(bean);
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
