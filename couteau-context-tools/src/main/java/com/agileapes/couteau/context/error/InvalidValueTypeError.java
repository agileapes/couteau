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
 * This error indicates that we have asked a reader to read values of a type it should not be
 * able to read.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/4/13, 11:33 AM)
 */
public class InvalidValueTypeError extends ValueReaderError {

    private static final String MESSAGE = "This value reader is not capable of reading values of type: %s";
    private final Class<?> type;

    /**
     * Instantiates the exception
     * @param type    the input type expected of the value reader to handle
     */
    public InvalidValueTypeError(Class<?> type) {
        super(String.format(MESSAGE, type.getCanonicalName()));
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
}
