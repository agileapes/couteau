package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import com.agileapes.couteau.reflection.util.assets.MemberModifierFilter;
import com.agileapes.couteau.reflection.util.assets.Modifiers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 2:45 PM)
 */
public class FieldClassBeanDescriptor<E> extends AbstractClassBeanDescriptor<E> {

    public FieldClassBeanDescriptor(Class<E> beanClass) {
        super(beanClass);
    }

    @Override
    protected Map<String, Class<?>> getProperties() throws Exception {
        final HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
        ReflectionUtils.withFields(getBeanType()).drop(new MemberModifierFilter(Modifiers.STATIC)).each(new Processor<Field>() {
            @Override
            public void process(Field input) throws Exception {
                map.put(input.getName(), input.getType());
            }
        });
        return map;
    }

}
