package com.agileapes.couteau.context.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:18 PM)
 */
public class NoSuchItemException extends RegistryException {

    public NoSuchItemException(String name) {
        super("No such item has been registered: " + name);
    }

}
