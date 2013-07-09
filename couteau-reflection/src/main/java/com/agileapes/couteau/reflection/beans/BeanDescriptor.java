package com.agileapes.couteau.reflection.beans;

import com.agileapes.couteau.reflection.error.NoSuchPropertyException;

import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:43 PM)
 */
public interface BeanDescriptor<E> {

    Class<E> getBeanType();

    Set<String> getPropertyNames();

    Class<?> getPropertyType(String propertyName) throws NoSuchPropertyException;

    boolean hasProperty(String propertyName);

}
