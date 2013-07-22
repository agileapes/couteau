package com.agileapes.couteau.reflection.error;

/**
 * Means that an item of invalid type has been supplied
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 6:58 AM)
 */
public class InvalidInputTypeException extends BeanConversionException {

    public InvalidInputTypeException(Class<?> beanType) {
        super("Invalid bean type supplied for conversion: " + beanType);
    }
}
