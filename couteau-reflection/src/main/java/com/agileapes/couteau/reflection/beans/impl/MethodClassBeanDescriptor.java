package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.reflection.property.ReadPropertyAccessor;
import com.agileapes.couteau.reflection.property.WritePropertyAccessor;
import com.agileapes.couteau.reflection.property.impl.MethodReadPropertyAccessor;
import com.agileapes.couteau.reflection.property.impl.MethodWritePropertyAccessor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import com.agileapes.couteau.reflection.util.assets.GetterMethodFilter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Describes the properties of the given class object using its getter and setter methods.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
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
     * @throws Exception
     */
    @Override
    protected Map<String,ReadPropertyAccessor<?>> getReaders() throws Exception {
        final HashMap<String, ReadPropertyAccessor<?>> map = new HashMap<String, ReadPropertyAccessor<?>>();
        ReflectionUtils.withMethods(getBeanType()).keep(new GetterMethodFilter()).each(new Processor<Method>() {
            @Override
            public void process(Method input) throws Exception {
                final String propertyName = ReflectionUtils.getPropertyName(input.getName());
                //noinspection unchecked
                map.put(propertyName, new MethodReadPropertyAccessor(input, null));
            }
        });
        return map;
    }

    /**
     * @return a map of property names to property writers
     * @throws Exception
     */
    @Override
    protected Map<String, WritePropertyAccessor<?>> getWriters() throws Exception {
        final HashMap<String, WritePropertyAccessor<?>> map = new HashMap<String, WritePropertyAccessor<?>>();
        ReflectionUtils.withMethods(getBeanType()).keep(new GetterMethodFilter()).each(new Processor<Method>() {
            @Override
            public void process(Method input) throws Exception {
                final String propertyName = ReflectionUtils.getPropertyName(input.getName());
                //noinspection unchecked
                map.put(propertyName, new MethodWritePropertyAccessor(input, null));
            }
        });
        return map;
    }

}