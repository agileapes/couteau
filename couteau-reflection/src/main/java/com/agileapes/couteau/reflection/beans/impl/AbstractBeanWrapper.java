/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.reflection.beans.BeanWrapper;
import com.agileapes.couteau.reflection.beans.WriteAccessorAware;
import com.agileapes.couteau.reflection.error.NoSuchPropertyException;
import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.error.PropertyTypeMismatchException;
import com.agileapes.couteau.reflection.property.WritePropertyAccessor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>This implementation builds on the functionalities offered by {@link AbstractBeanAccessor}, while adding
 * implementations for methods introduced in {@link BeanWrapper} and {@link WriteAccessorAware}</p>
 *
 * <p>This implementation leaves you to override {@link #getWriteAccessors()}, which is expected to determine
 * which properties are available for writing to. This helps with implementing new bean wrappers, by simply
 * having to introduce the code necessary for extracting read/write accessor determination from the bean
 * being wrapped.</p>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 1:47 PM)
 */
public abstract class AbstractBeanWrapper<E> extends AbstractBeanAccessor<E> implements BeanWrapper<E>, WriteAccessorAware {

    /**
     * The map from property names to property write accessors
     */
    private final Map<String, WritePropertyAccessor<?>> writeAccessors = new HashMap<String, WritePropertyAccessor<?>>();

    /**
     * Instantiates the bean wrapper, taking in the bean to be wrapped
     * @param bean    the been being wrapped
     */
    public AbstractBeanWrapper(E bean) {
        super(bean);
        try {
            writeAccessors.putAll(getWriteAccessors());
        } catch (Exception ignored) {
        }
    }

    /**
     * This method is expected to return property writers for all properties writable by this bean wrapper.
     * @return the map of property names to property write accessors
     * @throws Exception
     */
    protected abstract Map<String, WritePropertyAccessor<?>> getWriteAccessors() throws Exception;

    /**
     * Determines whether the given property can be written to or whether it is just
     * a read-only property.
     * @param propertyName    the name of the property
     * @return {@code true} if the property is writable
     * @throws NoSuchPropertyException
     */
    @Override
    public boolean isWritable(String propertyName) throws NoSuchPropertyException {
        return writeAccessors.containsKey(propertyName);
    }

    /**
     * Determines whether the given property can be read from or whether read access
     * to this property is restricted.
     * @param propertyName    the name of the property
     * @return {@code true} if the property is readable
     * @throws NoSuchPropertyException
     */
    @Override
    public boolean isReadable(String propertyName) throws NoSuchPropertyException {
        if (!hasProperty(propertyName)) {
            throw new NoSuchPropertyException(getBeanType(), propertyName);
        }
        return super.hasProperty(propertyName);
    }

    /**
     * This method is used to set the value of available properties
     * @param propertyName     the name of the property
     * @param propertyValue    the new value of the property
     * @throws NoSuchPropertyException if this property does not exist
     * @throws PropertyAccessException if the property is immutable, or if errors occur while setting the value for the property
     * @throws PropertyTypeMismatchException if the type of the argument being passed is not valid
     */
    @Override
    public void setPropertyValue(String propertyName, Object propertyValue) throws NoSuchPropertyException, PropertyTypeMismatchException, PropertyAccessException {
        final WritePropertyAccessor accessor = propertyValue == null ? getPropertyWriter(propertyName) : getPropertyWriter(propertyName, propertyValue.getClass());
        //noinspection unchecked
        accessor.setPropertyValue(propertyValue);
    }

    /**
     * Will return a generic property writer for the given property
     * @param propertyName    the property name
     * @return the property writer
     * @throws NoSuchPropertyException
     */
    @Override
    public WritePropertyAccessor<?> getPropertyWriter(String propertyName) throws NoSuchPropertyException {
        return getPropertyWriter(propertyName, null);
    }

    /**
     * Will return a property writer for a property of the given name-type
     * @param propertyName    the name of the property
     * @param propertyType    the type of the property
     * @param <T>             the type parameter of the property
     * @return the property writer
     * @throws NoSuchPropertyException
     */
    @Override
    public <T> WritePropertyAccessor<T> getPropertyWriter(String propertyName, Class<T> propertyType) throws NoSuchPropertyException {
        if (!hasProperty(propertyName)) {
            throw new NoSuchPropertyException(getBeanType(), propertyName);
        }
        final WritePropertyAccessor<?> accessor = writeAccessors.get(propertyName);
        if (propertyType == null || ReflectionUtils.mapType(accessor.getPropertyType()).isAssignableFrom(ReflectionUtils.mapType(propertyType))) {
            //noinspection unchecked
            return (WritePropertyAccessor<T>) accessor;
        }
        throw new NoSuchPropertyException(getBeanType(), propertyName, propertyType);
    }

    /**
     * Determines whether this bean has a property with the given name
     * @param propertyName    the name of the property
     * @return {@code true} if the bean has this property, and {@code false} otherwise.
     */
    @Override
    public boolean hasProperty(String propertyName) {
        return super.hasProperty(propertyName) || writeAccessors.containsKey(propertyName);
    }

    /**
     * @return the names of the properties available to this descriptor
     */
    @Override
    public Set<String> getPropertyNames() {
        final HashSet<String> propertyNames = new HashSet<String>(super.getPropertyNames());
        propertyNames.addAll(writeAccessors.keySet());
        return propertyNames;
    }

    /**
     * Determines the type of the given property
     * @param propertyName    the name of the property
     * @return this property's type
     * @throws NoSuchPropertyException
     */
    @Override
    public Class<?> getPropertyType(String propertyName) throws NoSuchPropertyException {
        if (writeAccessors.containsKey(propertyName)) {
            return getPropertyWriter(propertyName).getPropertyType();
        }
        return super.getPropertyType(propertyName);
    }

}
