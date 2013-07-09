package com.agileapes.couteau.reflection.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/30/12)
 */
public class PropertyTypeMismatchException extends Exception {

    public PropertyTypeMismatchException(Class<?> beanClass, String property) {
        super("Mismatched property type for " + beanClass.getCanonicalName() + ":" + property);
    }
}
