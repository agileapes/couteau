package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.reflection.beans.BeanWrapper;
import com.agileapes.couteau.reflection.beans.BeanWrapperFactory;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:08 AM)
 */
public class MethodBeanWrapperFactory implements BeanWrapperFactory {
    @Override
    public <E> BeanWrapper<E> getBeanWrapper(E bean) {
        return new MethodBeanWrapper<E>(bean);
    }
}
