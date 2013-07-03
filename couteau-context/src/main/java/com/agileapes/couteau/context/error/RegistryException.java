package com.agileapes.couteau.context.error;

/**
 * This abstract exception serves to distinguish all exceptions that are somehow related to the
 * internal workings of the registry and its descendants.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 3:47 PM)
 */
public abstract class RegistryException extends Exception {

    public RegistryException(String message) {
        super(message);
    }

    public RegistryException(String message, Throwable cause) {
        super(message, cause);
    }

}
