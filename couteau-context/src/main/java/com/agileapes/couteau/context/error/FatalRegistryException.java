package com.agileapes.couteau.context.error;

/**
 * This shows an error within a registry operation that is irreversible and thus fatal to the
 * registry. Such errors must be handled by either shutting the application down, or reviving the
 * registry anew.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/29, 1:15)
 */
public class FatalRegistryException extends RegistryException {

    /**
     * Instantiates the exception
     * @param message    the message explaining why a fatal error has occurred
     */
    public FatalRegistryException(String message) {
        super(message);
    }

    /**
     * Instantiates the exception, specifying the root cause of the error
     * @param message    the message explaining why a fatal error has occurred
     * @param cause      the cause of the error
     */
    public FatalRegistryException(String message, Throwable cause) {
        super(message, cause);
    }
}
