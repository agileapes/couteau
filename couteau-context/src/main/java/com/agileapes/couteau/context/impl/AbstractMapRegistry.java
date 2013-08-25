package com.agileapes.couteau.context.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * This extension to the abstract registry class allows for the expected hash operations to be
 * performed on a map accessible through {@link #getMap()} method.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/21/13, 4:28 PM)
 */
public abstract class AbstractMapRegistry<E> extends AbstractRegistry<E> {

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

}
