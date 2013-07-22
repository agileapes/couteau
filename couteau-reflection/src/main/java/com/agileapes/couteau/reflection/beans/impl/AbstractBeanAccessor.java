package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.reflection.beans.BeanAccessor;
import com.agileapes.couteau.reflection.beans.ReadAccessorAware;
import com.agileapes.couteau.reflection.error.NoSuchPropertyException;
import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.property.ReadPropertyAccessor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>This implementation of the {@link BeanAccessor} interface provides the basic functionalities expected
 * of a bean accessor, plus those offered by {@link ReadAccessorAware}. This is to facilitate the process
 * of developing new ways of accessing properties.</p>
 *
 * <p>To extend this abstract implementation, template method {@link #getReadAccessors()} has been provided,
 * through the implementation of which, one could easily provide the readers that will access properties.</p>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:51 PM)
 */
public abstract class AbstractBeanAccessor<E> implements BeanAccessor<E>, ReadAccessorAware {

    /**
     * The map of property names to property read accessors
     */
    private final Map<String, ReadPropertyAccessor<?>> accessors = new HashMap<String, ReadPropertyAccessor<?>>();

    /**
     * The wrapped bean, whose properties are being accessed
     */
    private final E bean;

    /**
     * The type of the wrapped bean
     */
    private final Class<E> beanType;

    /**
     * Instantiates the accessor, while taking the bean to be wrapped
     * @param bean    the bean to be wrapped. This can be later accessed through {@link #getBean()}
     */
    public AbstractBeanAccessor(final E bean) {
        this.bean = bean;
        //noinspection unchecked
        this.beanType = (Class<E>) bean.getClass();
        try {
            this.accessors.putAll(getReadAccessors());
        } catch (Exception ignored) {
        }
    }

    /**
     * This method should present property readers for all available properties in the wrapped bean
     * @return a map of property names to property readers
     * @throws Exception
     */
    protected abstract Map<String, ReadPropertyAccessor<?>> getReadAccessors() throws Exception;

    /**
     * Reads the value of the given property
     * @param propertyName    the name of the property
     * @return the value of the property
     * @throws NoSuchPropertyException if no property exists with the given name
     * @throws PropertyAccessException if there was a problem reading the value of the property
     */
    @Override
    public Object getPropertyValue(String propertyName) throws NoSuchPropertyException, PropertyAccessException {
        return getPropertyValue(propertyName, Object.class);
    }

    /**
     * Reads the value of the given property of the given type
     * @param propertyName      the name of the property
     * @param propertyType      the type of the property
     * @return the value of the property
     * @throws NoSuchPropertyException if no property exists of the given name-type pair
     * @throws PropertyAccessException if there was a problem reading the value of the property
     */
    @Override
    public <P> P getPropertyValue(String propertyName, Class<P> propertyType) throws NoSuchPropertyException, PropertyAccessException {
        return getPropertyReader(propertyName, propertyType).getPropertyValue();
    }

    /**
     * Exposes the underlying bean
     * @return the bean
     */
    @Override
    public E getBean() {
        return bean;
    }

    /**
     * @return the type of the bean being described
     */
    @Override
    public Class<E> getBeanType() {
        return beanType;
    }

    /**
     * @return the names of the properties available to this descriptor
     */
    @Override
    public Set<String> getPropertyNames() {
        return Collections.unmodifiableSet(accessors.keySet());
    }

    /**
     * Determines the type of the given property
     * @param propertyName    the name of the property
     * @return this property's type
     * @throws NoSuchPropertyException
     */
    @Override
    public Class<?> getPropertyType(String propertyName) throws NoSuchPropertyException {
        return getPropertyReader(propertyName).getPropertyType();
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
        return getPropertyReader(propertyName).getGenericPropertyType();
    }

    /**
     * Determines whether this bean has a property with the given name
     * @param propertyName    the name of the property
     * @return {@code true} if the bean has this property, and {@code false} otherwise.
     */
    @Override
    public boolean hasProperty(String propertyName) {
        return accessors.containsKey(propertyName);
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
        final ReadPropertyAccessor<?> accessor = accessors.get(propertyName);
        if  (ReflectionUtils.mapType(propertyType).isAssignableFrom(ReflectionUtils.mapType(accessor.getPropertyType()))) {
            //noinspection unchecked
            return (ReadPropertyAccessor<T>) accessor;
        }
        throw new NoSuchPropertyException(getBeanType(), propertyName, propertyType);
    }

}
