package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.basics.collections.CollectionWrapper;
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
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 2:07 PM)
 */
public class MethodBeanWrapper<E> extends AbstractBeanWrapper<E> {

    public MethodBeanWrapper(E bean) {
        super(bean);
    }

    @Override
    protected Map<String, WritePropertyAccessor<?>> getWriteAccessors() throws Exception {
        final HashMap<String, WritePropertyAccessor<?>> map = new HashMap<String, WritePropertyAccessor<?>>();
        ReflectionUtils.withMethods(getBeanType()).keep(new SetterMethodFilter()).each(new Processor<Method>() {
            @Override
            public void process(Method input) throws Exception {
                final String propertyName = ReflectionUtils.getPropertyName(input.getName());
                //noinspection unchecked
                map.put(propertyName, new MethodWritePropertyAccessor(propertyName, input.getParameterTypes()[0], input, getBean()));
            }
        });
        return map;
    }

    @Override
    protected Map<String, ReadPropertyAccessor<?>> getReadAccessors() throws Exception {
        final Map<String, ReadPropertyAccessor<?>> map = new HashMap<String, ReadPropertyAccessor<?>>();
        ReflectionUtils.withMethods(getBeanType()).keep(new GetterMethodFilter()).each(new Processor<Method>() {
            @Override
            public void process(Method input) throws Exception {
                final String propertyName = ReflectionUtils.getPropertyName(input.getName());
                //noinspection unchecked
                map.put(propertyName, new MethodReadPropertyAccessor(propertyName, input.getReturnType(), input, getBean()));
            }
        });
        return map;
    }

}
