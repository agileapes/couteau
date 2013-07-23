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
