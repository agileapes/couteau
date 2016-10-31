/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mmnaseri.couteau.context.impl;

import com.mmnaseri.couteau.context.contract.Registry;
import com.mmnaseri.couteau.context.error.*;

/**
 * This implementation implements all the facade operations basic to the workings of a
 * registry, leaving the implementation of the actual operations in charge of storing and
 * retrieving an item to extending classes. In other words, any operation related to the
 * internal storage must be implemented by extending classes.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:15 PM)
 */
public abstract class AbstractRegistry<E> implements Registry<E> {

    /**
     * This method is expected to be able to store a given item. All checks regarding the uniqueness
     * of the name and other relevant checks are assumed to be performed by the registry prior to
     * calling this method.
     * @param name    the name of the item
     * @param item    the instance
     * @return will return {@code true} if it could successfully write the item to the registry.
     */
    protected abstract boolean write(String name, E item);

    /**
     * This method will read an item (that is ensured to have been previously written to the registry)
     * from the internal storage.
     * @param name    the name of the item to be retrieved.
     * @return the item instance.
     */
    protected abstract E read(String name);

    /**
     * This method will remove the given bean from the registry. If the bean cannot be removed, then
     * it should return false
     * @param name    the name of the bean being queried.
     * @return <code>true</code> if the bean exists and has been removed from the underlying storage.
     */
    protected abstract boolean remove(String name);

    /**
     * Will register the given item with the given name. The name must be unique, and no other
     * item with this name must be already registered.
     * @param name    the name of the bean to be registered
     * @param item    the instance
     * @throws RegistryException
     */
    @Override
    public void register(String name, E item) throws RegistryException {
        if (item == null) {
            throw new NullPointerException("Item cannot be null");
        }
        if (contains(name)) {
            throw new DuplicateItemException(name);
        }
        if (!write(name, item)) {
            throw new RegistrationFailedException(name);
        }
    }

    /**
     * Removes the bean with the given name from the registry, if it exists. Elicits an exception
     * if no such bean exists or if there is a problem with removing the named bean.
     * @param name    the name of the bean being queried.
     * @throws RegistryException
     */
    @Override
    public void unregister(String name) throws RegistryException {
        if (name == null) {
            throw new NullPointerException("Item cannot be null");
        }
        if (contains(name)) {
            throw new DuplicateItemException(name);
        }
        if (!remove(name)) {
            throw new RegistrationFailedException(name);
        }
    }

    /**
     * Replaces an item with a new instance. Atomic operation that calls on both {@link #register(String, Object)}
     * and {@link #unregister(String)} methods in one go
     * @param name    the name of the bean to be registered
     * @param item    the instance
     * @throws RegistryException
     */
    @Override
    public synchronized void replace(String name, E item) throws RegistryException {
        unregister(name);
        register(name, item);
    }

    /**
     * Will return the bean with the given name. If the bean is not present, an exception will
     * be thrown.
     * @param name    the name of the bean being queried.
     * @return the bean instance
     * @throws RegistryException
     */
    @Override
    public E get(String name) throws RegistryException {
        return get(name, getRegistryType());
    }

    /**
     * Works with the same semantics as {@link #get(String)}. The only difference is that
     * the item must be of the given type or an exception will be raised.
     * @param name    the name to look for
     * @param type    the desired type of the item
     * @param <T>     the type parameter for the return type
     * @return the bean instance
     * @throws RegistryException
     */
    @Override
    public <T extends E> T get(String name, Class<T> type) throws RegistryException {
        if (!contains(name)) {
            throw new NoSuchItemException(name);
        }
        final E item = read(name);
        if (!type.isInstance(item)) {
            throw new InvalidBeanTypeException(name, type, item.getClass());
        }
        return type.cast(item);
    }

}
