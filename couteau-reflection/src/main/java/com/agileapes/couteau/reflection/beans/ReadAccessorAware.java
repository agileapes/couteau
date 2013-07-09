package com.agileapes.couteau.reflection.beans;

import com.agileapes.couteau.reflection.error.NoSuchPropertyException;
import com.agileapes.couteau.reflection.property.ReadPropertyAccessor;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:52 PM)
 */
public interface ReadAccessorAware {

    ReadPropertyAccessor<?> getPropertyReader(String propertyName) throws NoSuchPropertyException;

    <T> ReadPropertyAccessor<T> getPropertyReader(String propertyName, Class<T> propertyType) throws NoSuchPropertyException;

}
