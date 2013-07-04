package com.agileapes.couteau.context.error;

/**
 * This is a general error indicating that the fed value did not follow the rules
 * we expected it to.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/4/13, 11:34 AM)
 */
public class InvalidInputValueError extends ValueReaderError {

    protected static String createMessage(String value, Class<?> type) {
        return "Specified value (" + value + ") is not a valid representation of type " + type.getCanonicalName();
    }

    public InvalidInputValueError(String value, Class<?> type) {
        this(createMessage(value, type));
    }

    public InvalidInputValueError(String value, Class<?> type, Throwable cause) {
        this(createMessage(value, type), cause);
    }

    public InvalidInputValueError(String message) {
        super(message);
    }

    public InvalidInputValueError(String message, Throwable cause) {
        super(message, cause);
    }

}
