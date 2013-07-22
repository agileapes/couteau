package com.agileapes.couteau.reflection.beans;

/**
 * This is a factory that will dispense accessors for given beans
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 6:52 AM)
 */
public interface BeanAccessorFactory {

    /**
     * Returns bean accessors for the given bean
     * @param bean    the bean
     * @param <E>     type of the bean
     * @return the accessor to the bean
     */
    <E> BeanAccessor<E> getBeanAccessor(E bean);

}
