package com.agileapes.couteau.context.impl;

import com.agileapes.couteau.context.contract.Registry;
import com.agileapes.couteau.context.error.*;

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
        return get(name, getContextType());
    }

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
