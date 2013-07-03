package com.agileapes.couteau.context.error;

/**
 * This exception indicates an attempt to register an item with a registry with a name that
 * has been already used.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:17 PM)
 */
public class DuplicateItemException extends RegistryException {

    public DuplicateItemException(String name) {
        super("Another item with this name already exists: " + name);
    }

}
