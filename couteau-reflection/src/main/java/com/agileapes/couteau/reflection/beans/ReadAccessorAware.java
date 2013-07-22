package com.agileapes.couteau.reflection.beans;

import com.agileapes.couteau.reflection.error.NoSuchPropertyException;
import com.agileapes.couteau.reflection.property.ReadPropertyAccessor;

/**
 * Classes implementing this interface are able to expose the read accessors that can be used
 * to read property values from a given object.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:52 PM)
 */
public interface ReadAccessorAware {

    /**
     * Will return the generic property reader for the property named as specified
     * @param propertyName    the name of the property
     * @return the property reader
     * @throws NoSuchPropertyException
     */
    ReadPropertyAccessor<?> getPropertyReader(String propertyName) throws NoSuchPropertyException;

    /**
     * Will return the property reader for a property of the given name and type
     * @param propertyName    the name of the property
     * @param propertyType    the type of the property
     * @param <T>             the type parameter for the property
     * @return the property reader
     * @throws NoSuchPropertyException
     */
    <T> ReadPropertyAccessor<T> getPropertyReader(String propertyName, Class<T> propertyType) throws NoSuchPropertyException;

}
