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
 * This indicates that an invalid name has been assigned to the bean. The details of the
 * exception must be sought out in the message.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:38 PM)
 */
public class InvalidBeanNameException extends RegistryException {

    private static final String MESSAGE = "Expected bean to be named <%s> but it was named <%s>";
    private final String expected;
    private final String actual;

    /**
     * Instantiates the exception
     * @param expected    the name expected to be assigned to the bean
     * @param actual      the name actually given to the bean
     */
    public InvalidBeanNameException(String expected, String actual) {
        super(String.format(MESSAGE, expected, actual));
        this.expected = expected;
        this.actual = actual;
    }

    public String getExpected() {
        return expected;
    }

    public String getActual() {
        return actual;
    }
}
