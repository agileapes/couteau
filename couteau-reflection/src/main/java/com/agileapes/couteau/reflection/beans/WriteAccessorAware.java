package com.agileapes.couteau.reflection.beans;

import com.agileapes.couteau.reflection.error.NoSuchPropertyException;
import com.agileapes.couteau.reflection.property.WritePropertyAccessor;

/**
 * Classes implementing this interface are able to expose writers for the properties they are
 * working with.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:53 PM)
 */
public interface WriteAccessorAware {

    /**
     * Will return a generic property writer for the given property
     * @param propertyName    the property name
     * @return the property writer
     * @throws NoSuchPropertyException
     */
    WritePropertyAccessor<?> getPropertyWriter(String propertyName) throws NoSuchPropertyException;

    /**
     * Will return a property writer for a property of the given name-type
     * @param propertyName    the name of the property
     * @param propertyType    the type of the property
     * @param <T>             the type parameter of the property
     * @return the property writer
     * @throws NoSuchPropertyException
     */
    <T> WritePropertyAccessor<T> getPropertyWriter(String propertyName, Class<T> propertyType) throws NoSuchPropertyException;

}
