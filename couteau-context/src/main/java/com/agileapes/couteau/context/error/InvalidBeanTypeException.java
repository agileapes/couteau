package com.agileapes.couteau.context.error;

/**
 * This exception shows that the bean type being provided by the registry is not the one
 * expected by the end user.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/29, 13:51)
 */
public class InvalidBeanTypeException extends RegistryException {

    private static final String MESSAGE = "Expected bean <%s> to be of type <%s> while it was of type <%s>";
    private final String beanName;
    private final Class<?> expectedType;
    private final Class<?> actualType;

    /**
     * Instantiates the exception
     * @param beanName        the name of the bean causing the error
     * @param expectedType    the type expected of the bean
     * @param actualType      the actual type of the bean
     */
    public InvalidBeanTypeException(String beanName, Class<?> expectedType, Class<?> actualType) {
        super(String.format(MESSAGE, beanName, expectedType.getCanonicalName(), actualType));
        this.beanName = beanName;
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    public String getBeanName() {
        return beanName;
    }

    public Class<?> getExpectedType() {
        return expectedType;
    }

    public Class<?> getActualType() {
        return actualType;
    }

}
