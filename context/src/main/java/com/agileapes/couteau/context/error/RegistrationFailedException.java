package com.agileapes.couteau.context.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:18 PM)
 */
public class RegistrationFailedException extends RegistryException {

    public RegistrationFailedException(String name) {
        super("Failed to register bean with name: " + name);
    }

}
