package com.agileapes.couteau.context.error;

import java.util.Arrays;

/**
 * Indicates that specified constant did not match any of the enum constants we expected.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/4/13, 11:38 AM)
 */
public class InvalidEnumValueError extends InvalidInputValueError {

    private static final String MESSAGE = "Expected value (%s) to be one of %s";
    private final String value;
    private final Class<?> expectedType;

    /**
     * Instantiates the exception
     * @param value    the value being read
     * @param expectedType     the expected type against which the value is being evaluated
     */
    public InvalidEnumValueError(String value, Class<?> expectedType) {
        super(String.format(MESSAGE, value, Arrays.toString(expectedType.getEnumConstants())));
        this.value = value;
        this.expectedType = expectedType;
    }

    public String getValue() {
        return value;
    }

    public Class<?> getExpectedType() {
        return expectedType;
    }

}
