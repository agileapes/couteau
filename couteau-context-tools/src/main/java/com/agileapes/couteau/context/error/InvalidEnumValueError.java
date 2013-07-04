package com.agileapes.couteau.context.error;

import java.util.Arrays;

/**
 * Indicates that specified constant did not match any of the enum constants we expected.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/4/13, 11:38 AM)
 */
public class InvalidEnumValueError extends InvalidInputValueError {

    public InvalidEnumValueError(String value, Class<?> type) {
        super("Expected value (" + value + ") to be one of " + Arrays.toString(type.getEnumConstants()));
    }

}
