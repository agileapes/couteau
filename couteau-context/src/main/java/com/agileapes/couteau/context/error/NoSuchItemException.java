package com.agileapes.couteau.context.error;

/**
 * This exception indicates an attempt to retrieve an item by beanName that has not been registered
 * with the registry.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:18 PM)
 */
public class NoSuchItemException extends RegistryException {

    private static final String MESSAGE = "No such item has been registered: %s";
    private final String beanName;

    /**
     * Instantiates the exception
     * @param beanName    the name of the bean being looked up.
     */
    public NoSuchItemException(String beanName) {
        super(String.format(MESSAGE, beanName));
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
