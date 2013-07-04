package com.agileapes.couteau.context.error;

/**
 * This is the general basis for any errors inside the value reader that are actually related to
 * the process of reading a value.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/4/13, 11:33 AM)
 */
public class ValueReaderError extends RuntimeException {

    public ValueReaderError(String message) {
        super(message);
    }

    public ValueReaderError(String message, Throwable cause) {
        super(message, cause);
    }

}
