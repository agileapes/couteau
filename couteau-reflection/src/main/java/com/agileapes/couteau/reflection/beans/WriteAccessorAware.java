package com.agileapes.couteau.reflection.beans;

import com.agileapes.couteau.reflection.error.NoSuchPropertyException;
import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.property.WritePropertyAccessor;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:53 PM)
 */
public interface WriteAccessorAware {

    Object getPropertyWriter(String propertyName) throws NoSuchPropertyException, PropertyAccessException;

    <T> WritePropertyAccessor<T> getPropertyWriter(String propertyName, Class<T> propertyType) throws NoSuchPropertyException;

}
