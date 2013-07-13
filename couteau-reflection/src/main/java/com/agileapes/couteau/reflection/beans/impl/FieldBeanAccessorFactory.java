package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.reflection.beans.BeanAccessor;
import com.agileapes.couteau.reflection.beans.BeanAccessorFactory;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 6:52 AM)
 */
public class FieldBeanAccessorFactory implements BeanAccessorFactory {

    @Override
    public <E> BeanAccessor<E> getBeanAccessor(E bean) {
        return new FieldBeanAccessor<E>(bean);
    }

}
