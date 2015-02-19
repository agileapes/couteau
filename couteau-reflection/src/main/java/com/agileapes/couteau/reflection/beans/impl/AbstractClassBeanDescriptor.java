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

import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.reflection.beans.BeanDescriptor;
import com.agileapes.couteau.reflection.beans.ReadAccessorAware;
import com.agileapes.couteau.reflection.beans.WriteAccessorAware;
import com.agileapes.couteau.reflection.error.NoSuchPropertyException;
import com.agileapes.couteau.reflection.property.ReadPropertyAccessor;
import com.agileapes.couteau.reflection.property.WritePropertyAccessor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a bean descriptor that will take in a bean class and then project all its properties through the use of
 * read/write accessors.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 2:10 PM)
 */
public abstract class AbstractClassBeanDescriptor<E> implements BeanDescriptor<E>, ReadAccessorAware, WriteAccessorAware {

    /**
     * The bean class being described
     */
    private final Class<E> beanClass;

    /**
     * The property readers for this descriptor
     */
    private final Map<String, ReadPropertyAccessor<?>> readers = new HashMap<String, ReadPropertyAccessor<?>>();

    /**
     * The property writers for this descriptors
     */
    private final Map<String, WritePropertyAccessor<?>> writers = new HashMap<String, WritePropertyAccessor<?>>();

    /**
     * Instantiates the bean descriptor while taking in the bean class
     * @param beanClass    the class to be used
     */
    public AbstractClassBeanDescriptor(Class<E> beanClass) {
        this(beanClass, null);
    }

    /**
     * Instantiates the bean descriptor while taking in the bean class
     * @param beanClass    the class to be used
     */
    protected AbstractClassBeanDescriptor(Class<E> beanClass, Processor<AbstractClassBeanDescriptor<E>> processor) {
        if (processor != null) {
            processor.process(this);
        }
        this.beanClass = beanClass;
        this.readers.putAll(getReaders());
        this.writers.putAll(getWriters());
    }

    /**
     * @return a map of property names to property readers
     */
    protected abstract Map<String, ReadPropertyAccessor<?>> getReaders();

    /**
     * @return a map of property names to property writers
     */
    protected abstract Map<String, WritePropertyAccessor<?>> getWriters();

    /**
     * @return the type of the bean being described
     */
    @Override
    public Class<E> getBeanType() {
        return beanClass;
    }

    /**
     * @return the names of the properties available to this descriptor
     */
    @Override
    public Set<String> getPropertyNames() {
        return readers.keySet();
    }

    /**
     * Determines the type of the given property
     * @param propertyName    the name of the property
     * @return this property's type
     * @throws NoSuchPropertyException
     */
    @Override
    public Class<?> getPropertyType(String propertyName) throws NoSuchPropertyException {
        if (!hasProperty(propertyName)) {
            throw new NoSuchPropertyException(getBeanType(), propertyName);
        }
        return readers.containsKey(propertyName) ? readers.get(propertyName).getPropertyType() : writers.get(propertyName).getPropertyType();
    }

    /**
     * Determines whether this bean has a property with the given name
     * @param propertyName    the name of the property
     * @return {@code true} if the bean has this property, and {@code false} otherwise.
     */
    @Override
    public boolean hasProperty(String propertyName) {
        return readers.containsKey(propertyName) || writers.containsKey(propertyName);
    }

    /**
     * Will return the generic property reader for the property named as specified
     * @param propertyName    the name of the property
     * @return the property reader
     * @throws NoSuchPropertyException
     */
    @Override
    public ReadPropertyAccessor<?> getPropertyReader(String propertyName) throws NoSuchPropertyException {
        return getPropertyReader(propertyName, Object.class);
    }

    /**
     * Will return the property reader for a property of the given name and type
     * @param propertyName    the name of the property
     * @param propertyType    the type of the property
     * @param <T>             the type parameter for the property
     * @return the property reader
     * @throws NoSuchPropertyException
     */
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

    /**
     * Will return a generic property writer for the given property
     * @param propertyName    the property name
     * @return the property writer
     * @throws NoSuchPropertyException
     */
    @Override
    public WritePropertyAccessor<?> getPropertyWriter(String propertyName) throws NoSuchPropertyException {
        return getPropertyWriter(propertyName, Object.class);
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

    /**
     * Determines whether the given property can be written to or whether it is just
     * a read-only property.
     * @param propertyName    the name of the property
     * @return {@code true} if the property is writable
     * @throws NoSuchPropertyException
     */
    public boolean isWritable(String propertyName) throws NoSuchPropertyException {
        if (!hasProperty(propertyName)) {
            throw new NoSuchPropertyException(getBeanType(), propertyName);
        }
        return writers.containsKey(propertyName);
    }

    /**
     * Will return the generic type of the property. If the property does not define a generic
     * type, this method's return value will be equivalent to that of {@link #getPropertyType(String)}
     * @param propertyName    the name of the property
     * @return the generic type of the property
     * @throws NoSuchPropertyException
     */
    @Override
    public Type getGenericPropertyType(String propertyName) throws NoSuchPropertyException {
        return readers.containsKey(propertyName) ? getPropertyReader(propertyName).getGenericPropertyType() : getPropertyWriter(propertyName).getGenericPropertyType();
    }

    /**
     * Determines whether the wrapped class is abstract or not
     * @return {@code true} if the wrapped class is abstract or if it is an interface
     */
    public boolean isAbstract() {
        return Modifier.isAbstract(beanClass.getModifiers()) || beanClass.isInterface();
    }

}
