package com.agileapes.couteau.reflection.beans;

import com.agileapes.couteau.reflection.error.BeanInstantiationException;

/**
 * The bean initializer interfaces the process of initializing a bean from a specific type
 * using a given constructor signature.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/4/13, 6:54 PM)
 */
public interface BeanInitializer {

    /**
     * <p>This method will initialize a bean of the given type using the specified initialization
     * arguments.</p>
     *
     * <p>Any errors occurring during this process will be reflected as a BeanInstantiationException
     * to simplify handing exceptions for bean initialization.</p>
     * @param type             the type of the bean
     * @param argumentTypes    the types of the arguments being passed
     * @param arguments        the arguments used for initialization of the bean
     * @param <E>              the type of the bean
     * @return an instance of the bean in question
     * @throws BeanInstantiationException
     */
    <E> E initialize(Class<E> type, Class[] argumentTypes, Object[] arguments) throws BeanInstantiationException;

}
