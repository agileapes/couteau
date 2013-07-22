package com.agileapes.couteau.reflection.error;

/**
 * This indicates an illegal access request to a bean's properties
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/24/12)
 */
public class PropertyAccessException extends Exception {

    public PropertyAccessException(Class<?> beanClass, String property, Throwable cause) {
        super("Error accessing property " + beanClass.getCanonicalName() + ":" + property, cause);
    }

}
