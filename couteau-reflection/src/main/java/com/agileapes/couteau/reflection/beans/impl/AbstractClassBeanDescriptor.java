package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.reflection.beans.BeanDescriptor;
import com.agileapes.couteau.reflection.error.NoSuchPropertyException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 2:10 PM)
 */
public abstract class AbstractClassBeanDescriptor<E> implements BeanDescriptor<E> {

    private final Class<E> beanClass;
    private final Map<String, Class<?>> properties = new HashMap<String, Class<?>>();

    public AbstractClassBeanDescriptor(Class<E> beanClass) {
        this.beanClass = beanClass;
        try {
            this.properties.putAll(getProperties());
        } catch (Exception ignored) {
        }
    }

    protected abstract Map<String, Class<?>> getProperties() throws Exception;

    @Override
    public Class<E> getBeanType() {
        return beanClass;
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public Class<?> getPropertyType(String propertyName) throws NoSuchPropertyException {
        if (!hasProperty(propertyName)) {
            throw new NoSuchPropertyException(getBeanType(), propertyName);
        }
        return properties.get(propertyName);
    }

    @Override
    public boolean hasProperty(String propertyName) {
        return properties.containsKey(propertyName);
    }
}
