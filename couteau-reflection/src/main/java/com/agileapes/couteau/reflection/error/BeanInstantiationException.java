package com.agileapes.couteau.reflection.error;

/**
 * Denotes an error occurring during the process of bean instantiation
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/4/13, 6:59 PM)
 */
public class BeanInstantiationException extends Exception {

    public BeanInstantiationException(String message) {
        super(message);
    }

    public BeanInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
