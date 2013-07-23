/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

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
