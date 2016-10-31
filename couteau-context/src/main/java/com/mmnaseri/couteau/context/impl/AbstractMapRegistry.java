/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
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

import com.mmnaseri.couteau.reflection.util.ClassUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * This extension to the abstract registry class allows for the expected hash operations to be
 * performed on a map accessible through {@link #getMap()} method.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (8/21/13, 4:28 PM)
 */
public abstract class AbstractMapRegistry<E> extends AbstractRegistry<E> {

    private final Class<E> registryType;

    public AbstractMapRegistry() {
        //noinspection unchecked
        registryType = (Class<E>) ClassUtils.resolveTypeArgument(getClass(), AbstractMapRegistry.class);
    }

    /**
     * @return the map that will be used for the store of the registry
     */
    protected abstract Map<String, E> getMap();

    /**
     * Attempts to write the given item to the underlying map, checking for the validity of the
     * operation by checking whether the write operation was successful.
     * @param name    the name of the item
     * @param item    the instance
     * @return {@code true} if the operation was successful
     */
    @Override
    protected final boolean write(String name, E item) {
        getMap().put(name, item);
        return contains(name);
    }

    /**
     * Reads the item having been given the specified qualifier
     * @param name    the name of the item to be retrieved.
     * @return the item
     */
    @Override
    protected final E read(String name) {
        return getMap().get(name);
    }

    @Override
    protected boolean remove(String name) {
        getMap().remove(name);
        return !contains(name);
    }

    /**
     * Checks whether or not the map exposed to the registry contains the given item
     * @param name    the name of the object
     * @return {@code true} if the item exists in the registry
     */
    @Override
    public boolean contains(String name) {
        return getMap().containsKey(name);
    }

    /**
     * @return a collection of all the beans registered with the registry. Modifying this
     * collection should have no effects on the registry.
     */
    @Override
    public Collection<E> getBeans() {
        return Collections.unmodifiableCollection(getMap().values());
    }

    /**
     * @return a collection of the names of all the beans in this registry. Modifying this
     * collection should have no effects on the registry.
     */
    @Override
    public Collection<String> getBeanNames() {
        return Collections.unmodifiableCollection(getMap().keySet());
    }

    /**
     * Determines the super-type of objects that can be registered with this registry.
     * @return the type of objects in the registry
     */
    @Override
    public Class<E> getRegistryType() {
        return registryType;
    }

}
