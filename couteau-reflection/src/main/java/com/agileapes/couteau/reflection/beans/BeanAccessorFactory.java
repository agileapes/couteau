package com.agileapes.couteau.reflection.beans;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 6:52 AM)
 */
public interface BeanAccessorFactory {

    <E> BeanAccessor<E> getBeanAccessor(E bean);

}
