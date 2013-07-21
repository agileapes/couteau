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
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 2:45 PM)
 */
public class FieldClassBeanDescriptor<E> extends AbstractClassBeanDescriptor<E> {

    public FieldClassBeanDescriptor(Class<E> beanClass) {
        super(beanClass);
    }

    @Override
    protected Map<String, ReadPropertyAccessor<?>> getReaders() throws Exception {
        final HashMap<String, ReadPropertyAccessor<?>> map = new HashMap<String, ReadPropertyAccessor<?>>();
        ReflectionUtils.withFields(getBeanType()).drop(new MemberModifierFilter(Modifiers.STATIC)).each(new Processor<Field>() {
            @Override
            public void process(Field input) throws Exception {
                //noinspection unchecked
                map.put(input.getName(), new FieldReadPropertyAccessor(input.getName(), input.getType(), input, null));
            }
        });
        return map;
    }

    @Override
    protected Map<String, WritePropertyAccessor<?>> getWriters() throws Exception {
        final HashMap<String, WritePropertyAccessor<?>> map = new HashMap<String, WritePropertyAccessor<?>>();
        ReflectionUtils.withFields(getBeanType()).drop(new MemberModifierFilter(Modifiers.STATIC, Modifiers.FINAL)).each(new Processor<Field>() {
            @Override
            public void process(Field input) throws Exception {
                //noinspection unchecked
                map.put(input.getName(), new FieldWritePropertyAccessor(input.getName(), input.getType(), input, null));
            }
        });
        return map;
    }

}
