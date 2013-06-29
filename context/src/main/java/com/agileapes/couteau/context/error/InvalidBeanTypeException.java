package com.agileapes.couteau.context.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/29, 13:51)
 */
public class InvalidBeanTypeException extends RegistryException {
    public InvalidBeanTypeException(String beanName, Class<?> expectedType, Class<?> actualType) {
        super("Expected bean <" + beanName + "> to be of type <" + expectedType.getCanonicalName() + "> while it was of type <" + actualType + ">");
    }
}
