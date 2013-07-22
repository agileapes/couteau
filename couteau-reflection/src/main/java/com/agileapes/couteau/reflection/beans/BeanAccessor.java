package com.agileapes.couteau.reflection.beans;

import com.agileapes.couteau.reflection.error.NoSuchPropertyException;
import com.agileapes.couteau.reflection.error.PropertyAccessException;

/**
 * This interface will add to the capabilities  of {@link BeanDescriptor} by enabling interface
 * methods that will give access to properties in the underlying bean. The bean accessor is basically
 * a gate to read-only access to a bean's properties.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:45 PM)
 */
public interface BeanAccessor<E> extends BeanDescriptor<E> {

    /**
     * Reads the value of the given property
     * @param propertyName    the name of the property
     * @return the value of the property
     * @throws NoSuchPropertyException if no property exists with the given name
     * @throws PropertyAccessException if there was a problem reading the value of the property
     */
    Object getPropertyValue(String propertyName) throws NoSuchPropertyException, PropertyAccessException;

    /**
     * Reads the value of the given property of the given type
     * @param propertyName      the name of the property
     * @param propertyType      the type of the property
     * @return the value of the property
     * @throws NoSuchPropertyException if no property exists of the given name-type pair
     * @throws PropertyAccessException if there was a problem reading the value of the property
     */
    <P> P getPropertyValue(String propertyName, Class<P> propertyType) throws NoSuchPropertyException, PropertyAccessException;

    /**
     * Exposes the underlying bean
     * @return the bean
     */
    E getBean();

}
