package com.agileapes.couteau.reflection.error;

/**
 * This exception is thrown when a non-existent property is being accessed
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0, (7/24/12, 1:10 PM)
 */
public class NoSuchPropertyException extends Exception {

    public NoSuchPropertyException(Class<?> beanClass, String property) {
        super("No such property " + beanClass.getCanonicalName() + ":" + property);
    }

    public NoSuchPropertyException(Class<?> beanClass, String property, Class<?> type) {
        super("No such property " + beanClass.getCanonicalName() + ":" + property + " of type " + type);
    }

}
