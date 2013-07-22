package com.agileapes.couteau.context.error;

/**
 * <p>This abstract exception serves to distinguish all exceptions that are somehow related to the
 * internal workings of the registry and its descendants.</p>
 *
 * <p>Registry exceptions must always have a verbose message that specifies exactly what has led
 * to this error being raised.</p>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 3:47 PM)
 */
public abstract class RegistryException extends Exception {

    /**
     * Instantiates the exception
     * @param message    the reason for the error
     */
    public RegistryException(String message) {
        super(message);
    }

    /**
     * Instantiates the exception, specifying the root cause of the error, which can be further examined, if necessary.
     * @param message    the reason for the error
     * @param cause      the root cause of the error
     */
    public RegistryException(String message, Throwable cause) {
        super(message, cause);
    }

}
