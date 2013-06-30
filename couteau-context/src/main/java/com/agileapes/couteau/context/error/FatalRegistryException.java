package com.agileapes.couteau.context.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/29, 1:15)
 */
public class FatalRegistryException extends RegistryException {

    public FatalRegistryException(String message) {
        super(message);
    }

    public FatalRegistryException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
