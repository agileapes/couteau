package com.agileapes.couteau.context.contract;

import com.agileapes.couteau.context.error.RegistryException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:11 PM)
 */
public interface BeanProcessor<E> {

    E postProcessBeforeRegistration(E bean, String name) throws RegistryException;

    E postProcessBeforeAccess(E bean, String name) throws RegistryException;

}
