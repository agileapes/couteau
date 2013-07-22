package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.reflection.beans.BeanAccessor;
import com.agileapes.couteau.reflection.beans.BeanAccessorFactory;
import com.agileapes.couteau.reflection.beans.BeanWrapperFactory;

/**
 * This is a convenience implementation that is designed to naturally convert a {@link BeanWrapperFactory}
 * to a {@link BeanAccessorFactory}, so that the former could be used in the place of the latter, even though
 * cases for such a requirement are extremely unlikely.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/22/13, 12:05 PM)
 */
public class BeanAccessorFactoryAdapter implements BeanAccessorFactory {

    private final BeanWrapperFactory wrapperFactory;

    public BeanAccessorFactoryAdapter(BeanWrapperFactory wrapperFactory) {
        this.wrapperFactory = wrapperFactory;
    }

    @Override
    public <E> BeanAccessor<E> getBeanAccessor(E bean) {
        return wrapperFactory.getBeanWrapper(bean);
    }

}
