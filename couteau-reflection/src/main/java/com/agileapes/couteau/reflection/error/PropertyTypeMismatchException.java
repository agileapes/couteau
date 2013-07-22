package com.agileapes.couteau.reflection.error;

/**
 * This error means that the property value being used and the actual property are of
 * different types
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/30/12)
 */
public class PropertyTypeMismatchException extends Exception {

    public PropertyTypeMismatchException(Class<?> beanClass, String property) {
        super("Mismatched property type for " + beanClass.getCanonicalName() + ":" + property);
    }
}
