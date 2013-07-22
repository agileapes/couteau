package com.agileapes.couteau.context.error;

/**
 * This exception indicates a general failure in the registration of a bean of the given name.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:18 PM)
 */
public class RegistrationFailedException extends RegistryException {

    private static final String MESSAGE = "Failed to register bean with name: %s";
    private final String beanName;

    /**
     * Instantiates the exception
     * @param beanName    the bean name the registration for which has failed.
     */
    public RegistrationFailedException(String beanName) {
        super(String.format(MESSAGE, beanName));
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
