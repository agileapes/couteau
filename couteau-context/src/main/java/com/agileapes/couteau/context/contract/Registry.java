/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.couteau.context.contract;

import com.agileapes.couteau.context.error.RegistryException;

import java.util.Collection;

/**
 * The registry is a place for storing beans of a given type with unique names. It is capable of
 * looking up the beans by name and also providing a collection of all available beans.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 3:45 PM)
 */
public interface Registry<E> {

    /**
     * Determines the supertype of objects that can be registered with this registry.
     * @return the type of objects in the registry
     */
    Class<E> getRegistryType();

    /**
     * Will determine whether an object of the given name has been registered with this registry
     * or not
     * @param name    the name of the object
     * @return {@code true} if an item with this name has been previously registered with the registry
     */
    boolean contains(String name);

    /**
     * Will register the given item with the given name. The name must be unique, and no other
     * item with this name must be already registered.
     * @param name    the name of the bean to be registered
     * @param item    the instance
     * @throws RegistryException
     */
    void register(String name, E item) throws RegistryException;

    /**
     * Removes the bean with the given name from the registry, if it exists. Elicits an exception
     * if no such bean exists or if there is a problem with removing the named bean.
     * @param name    the name of the bean being queried.
     * @throws RegistryException
     */
    void unregister(String name) throws RegistryException;

    /**
     * Will return the bean with the given name. If the bean is not present, an exception will
     * be thrown.
     * @param name    the name of the bean being queried.
     * @return the bean instance
     * @throws RegistryException
     */
    E get(String name) throws RegistryException;

    /**
     * Works with the same semantics as {@link #get(String)}. The only difference is that
     * the item must be of the given type or an exception will be raised.
     * @param name    the name to look for
     * @param type    the desired type of the item
     * @param <T>     the type parameter for the return type
     * @return the bean instance
     * @throws RegistryException
     */
    <T extends E> T get(String name, Class<T> type) throws RegistryException;

    /**
     * @return a collection of all the beans registered with the registry. Modifying this
     * collection should have no effects on the registry.
     */
    Collection<E> getBeans();

    /**
     * @return a collection of the names of all the beans in this registry. Modifying this
     * collection should have no effects on the registry.
     */
    Collection<String> getBeanNames();

}
