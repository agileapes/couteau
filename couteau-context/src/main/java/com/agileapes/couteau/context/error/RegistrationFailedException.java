package com.agileapes.couteau.context.error;

/**
 * This exception indicates a general failure in the registration of a bean of the given name.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:18 PM)
 */
public class RegistrationFailedException extends RegistryException {

    public RegistrationFailedException(String name) {
        super("Failed to register bean with name: " + name);
    }

}
