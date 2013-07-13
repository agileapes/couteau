package com.agileapes.couteau.reflection.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 6:48 AM)
 */
public abstract class BeanConversionException extends Exception {

    public BeanConversionException(String message) {
        super(message);
    }

    public BeanConversionException(String message, Throwable cause) {
        super(message, cause);
    }

}
