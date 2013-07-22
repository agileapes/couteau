package com.agileapes.couteau.reflection.beans;

/**
 * This factory takes in a bean and dispenses identifiable bean wrappers for it.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:08 AM)
 */
public interface BeanWrapperFactory {

    /**
     * Dispenses a bean wrapper for the given bean
     * @param bean    the bean to be wrapped
     * @param <E>     the type of the bean
     * @return the bean wrapper
     */
    <E> BeanWrapper<E> getBeanWrapper(E bean);

}
