package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.reflection.beans.BeanAccessor;
import com.agileapes.couteau.reflection.beans.BeanAccessorFactory;

/**
 * Returns {@link MethodBeanAccessor} for beans
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 6:53 AM)
 */
public class MethodBeanAccessorFactory implements BeanAccessorFactory {

    /**
     * Returns bean accessors for the given bean
     * @param bean    the bean
     * @param <E>     type of the bean
     * @return the accessor to the bean
     */
    @Override
    public <E> BeanAccessor<E> getBeanAccessor(E bean) {
        return new MethodBeanAccessor<E>(bean);
    }

}
