package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.reflection.beans.BeanWrapper;
import com.agileapes.couteau.reflection.beans.BeanWrapperFactory;

/**
 * Wraps beans using {@link FieldBeanWrapper}
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:08 AM)
 */
public class FieldBeanWrapperFactory implements BeanWrapperFactory {

    /**
     * Dispenses a bean wrapper for the given bean
     * @param bean    the bean to be wrapped
     * @param <E>     the type of the bean
     * @return the bean wrapper
     */
    @Override
    public <E> BeanWrapper<E> getBeanWrapper(E bean) {
        return new FieldBeanWrapper<E>(bean);
    }

}
