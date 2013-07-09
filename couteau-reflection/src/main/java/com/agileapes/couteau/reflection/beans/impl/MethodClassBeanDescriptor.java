package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import com.agileapes.couteau.reflection.util.assets.GetterMethodFilter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 2:15 PM)
 */
public class MethodClassBeanDescriptor<E> extends AbstractClassBeanDescriptor<E> {

    public MethodClassBeanDescriptor(Class<E> beanClass) {
        super(beanClass);
    }

    @Override
    protected Map<String, Class<?>> getProperties() throws Exception {
        final Map<String, Class<?>> map = new HashMap<String, Class<?>>();
        ReflectionUtils.withMethods(getBeanType()).keep(new GetterMethodFilter()).each(new Processor<Method>() {
            @Override
            public void process(Method input) throws Exception {
                map.put(ReflectionUtils.getPropertyName(input.getName()), input.getReturnType());
            }
        });
        return map;
    }

}