package com.agileapes.couteau.context.error;

/**
 * This error indicates that we have asked a reader to read values of a type it should not be
 * able to read.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/4/13, 11:33 AM)
 */
public class InvalidValueTypeError extends ValueReaderError {

    public InvalidValueTypeError(Class<?> type) {
        super("This value reader is not capable of reading values of type: " + type.getCanonicalName());
    }

}
