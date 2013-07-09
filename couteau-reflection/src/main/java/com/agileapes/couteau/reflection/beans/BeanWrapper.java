package com.agileapes.couteau.reflection.beans;

import com.agileapes.couteau.reflection.error.NoSuchPropertyException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:47 PM)
 */
public interface BeanWrapper<E> extends BeanAccessor<E>, ConfigurableBean {

    boolean isWritable(String propertyName) throws NoSuchPropertyException;

    boolean isReadable(String propertyName) throws NoSuchPropertyException;

}
