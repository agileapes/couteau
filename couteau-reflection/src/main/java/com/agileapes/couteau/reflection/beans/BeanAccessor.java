package com.agileapes.couteau.reflection.beans;

import com.agileapes.couteau.reflection.error.NoSuchPropertyException;
import com.agileapes.couteau.reflection.error.PropertyAccessException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:45 PM)
 */
public interface BeanAccessor<E> extends BeanDescriptor<E> {

    Object getPropertyValue(String propertyName) throws NoSuchPropertyException, PropertyAccessException;

    <P> P getPropertyValue(String propertyName, Class<P> propertyType) throws NoSuchPropertyException, PropertyAccessException;

    E getBean();

}
