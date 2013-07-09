package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.reflection.beans.BeanDescriptor;
import com.agileapes.couteau.reflection.beans.ReadAccessorAware;
import com.agileapes.couteau.reflection.beans.WriteAccessorAware;
import com.agileapes.couteau.reflection.error.NoSuchPropertyException;
import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.property.ReadPropertyAccessor;
import com.agileapes.couteau.reflection.property.WritePropertyAccessor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 2:10 PM)
 */
public abstract class AbstractClassBeanDescriptor<E> implements BeanDescriptor<E>, ReadAccessorAware, WriteAccessorAware {

    private final Class<E> beanClass;
    private final Map<String, ReadPropertyAccessor<?>> readers = new HashMap<String, ReadPropertyAccessor<?>>();
    private final Map<String, WritePropertyAccessor<?>> writers = new HashMap<String, WritePropertyAccessor<?>>();
    private final Map<String, Class<?>> properties = new HashMap<String, Class<?>>();

    public AbstractClassBeanDescriptor(Class<E> beanClass) {
        this.beanClass = beanClass;
        try {
            this.readers.putAll(getReaders());
            this.writers.putAll(getWriters());
        } catch (Exception ignored) {
        }
        for (Map.Entry<String, ReadPropertyAccessor<?>> entry : readers.entrySet()) {
            properties.put(entry.getKey(), entry.getValue().getPropertyType());
        }
        for (Map.Entry<String, WritePropertyAccessor<?>> entry : writers.entrySet()) {
            properties.put(entry.getKey(), entry.getValue().getPropertyType());
        }
    }

    protected abstract Map<String, ReadPropertyAccessor<?>> getReaders() throws Exception;

    protected abstract Map<String, WritePropertyAccessor<?>> getWriters() throws Exception;

    @Override
    public Class<E> getBeanType() {
        return beanClass;
    }

    @Override
    public Set<String> getPropertyNames() {
        return readers.keySet();
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

    @Override
    public ReadPropertyAccessor<?> getPropertyReader(String propertyName) throws NoSuchPropertyException {
        return getPropertyReader(propertyName, Object.class);
    }

    @Override
    public <T> ReadPropertyAccessor<T> getPropertyReader(String propertyName, Class<T> propertyType) throws NoSuchPropertyException {
        if (!hasProperty(propertyName)) {
            throw new NoSuchPropertyException(getBeanType(), propertyName);
        }
        final ReadPropertyAccessor<?> accessor = readers.get(propertyName);
        if (!ReflectionUtils.mapType(propertyType).isAssignableFrom(ReflectionUtils.mapType(accessor.getPropertyType()))) {
            throw new NoSuchPropertyException(getBeanType(), propertyName, propertyType);
        }
        //noinspection unchecked
        return (ReadPropertyAccessor<T>) accessor;
    }

    @Override
    public Object getPropertyWriter(String propertyName) throws NoSuchPropertyException, PropertyAccessException {
        return getPropertyWriter(propertyName, Object.class);
    }

    @Override
    public <T> WritePropertyAccessor<T> getPropertyWriter(String propertyName, Class<T> propertyType) throws NoSuchPropertyException {
        if (!hasProperty(propertyName)) {
            throw new NoSuchPropertyException(getBeanType(), propertyName);
        }
        if (!isWritable(propertyName)) {
            return null;
        }
        final WritePropertyAccessor<?> accessor = writers.get(propertyName);
        if (!ReflectionUtils.mapType(propertyType).isAssignableFrom(ReflectionUtils.mapType(accessor.getPropertyType()))) {
            throw new NoSuchPropertyException(getBeanType(), propertyName, propertyType);
        }
        //noinspection unchecked
        return (WritePropertyAccessor<T>) accessor;
    }

    public boolean isWritable(String propertyName) throws NoSuchPropertyException {
        if (!hasProperty(propertyName)) {
            throw new NoSuchPropertyException(getBeanType(), propertyName);
        }
        return writers.containsKey(propertyName);
    }

}
