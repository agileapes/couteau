package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.reflection.property.ReadPropertyAccessor;
import com.agileapes.couteau.reflection.property.impl.MethodReadPropertyAccessor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import com.agileapes.couteau.reflection.util.assets.GetterMethodFilter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Exposes properties for wrapped bean using all getter methods.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 1:15 PM)
 */
public class MethodBeanAccessor<E> extends AbstractBeanAccessor<E> {

    /**
     * Instantiates the accessor, while taking the bean to be wrapped
     * @param bean    the bean to be wrapped. This can be later accessed through {@link #getBean()}
     */
    public MethodBeanAccessor(E bean) {
        super(bean);
    }

    /**
     * This method should present property readers for all available properties in the wrapped bean
     * @return a map of property names to property readers
     * @throws Exception
     */
    @Override
    protected Map<String, ReadPropertyAccessor<?>> getReadAccessors() throws Exception {
        final HashMap<String, ReadPropertyAccessor<?>> map = new HashMap<String, ReadPropertyAccessor<?>>();
        ReflectionUtils.withMethods(getBeanType()).keep(new GetterMethodFilter()).each(new Processor<Method>() {
            @Override
            public void process(Method input) throws Exception {
                final String propertyName = ReflectionUtils.getPropertyName(input.getName());
                //noinspection unchecked
                map.put(propertyName, new MethodReadPropertyAccessor(input, getBean()));
            }
        });
        return map;
    }

}
