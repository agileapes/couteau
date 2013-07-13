package com.agileapes.couteau.reflection.beans;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:08 AM)
 */
public interface BeanWrapperFactory {

    <E> BeanWrapper<E> getBeanWrapper(E bean);

}
