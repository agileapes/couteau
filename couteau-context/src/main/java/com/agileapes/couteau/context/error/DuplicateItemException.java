package com.agileapes.couteau.context.error;

/**
 * This exception indicates an attempt to register an item with a registry with a name that
 * has been already used.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:17 PM)
 */
public class DuplicateItemException extends RegistryException {

    private static final String MESSAGE = "Another item with this name already exists: %s";
    private final String name;

    /**
     * Instantiates the exception
     * @param name    the name of the item being registered, for which a duplicate exists
     */
    public DuplicateItemException(String name) {
        super(String.format(MESSAGE, name));
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
