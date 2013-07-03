package com.agileapes.couteau.context.error;

/**
 * This exception indicates an attempt to retrieve an item by name that has not been registered
 * with the registry.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:18 PM)
 */
public class NoSuchItemException extends RegistryException {

    public NoSuchItemException(String name) {
        super("No such item has been registered: " + name);
    }

}
