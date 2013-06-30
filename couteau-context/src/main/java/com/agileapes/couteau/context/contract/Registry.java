package com.agileapes.couteau.context.contract;

import com.agileapes.couteau.context.error.RegistryException;

import java.util.Collection;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 3:45 PM)
 */
public interface Registry<E> {

    Class<E> getContextType();

    boolean contains(String name);

    void register(String name, E item) throws RegistryException;

    E get(String name) throws RegistryException;

    <T extends E> T get(String name, Class<T> type) throws RegistryException;

    Collection<E> getBeans();

    Collection<String> getBeanNames();

}