package com.agileapes.couteau.reflection.error;

/**
 * Denotes a fatal conversion exception
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:05 AM)
 */
public class FatalBeanConversionException extends BeanConversionException {

    public FatalBeanConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FatalBeanConversionException(String message) {
        super(message);
    }

}
