package com.agileapes.couteau.context.impl;

import com.agileapes.couteau.context.contract.Registry;
import com.agileapes.couteau.context.error.DuplicateItemException;
import com.agileapes.couteau.context.error.NoSuchItemException;
import com.agileapes.couteau.context.error.RegistrationFailedException;
import com.agileapes.couteau.context.error.RegistryException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:15 PM)
 */
public abstract class AbstractRegistry<E> implements Registry<E> {

    protected abstract boolean write(String name, E item);
    protected abstract E read(String name);

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

    @Override
    public E get(String name) throws RegistryException {
        if (!contains(name)) {
            throw new NoSuchItemException(name);
        }
        return read(name);
    }

}
