/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mmnaseri.couteau.context.error;

/**
 * This is a general error indicating that the fed value did not follow the rules
 * we expected it to.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/4/13, 11:34 AM)
 */
public class InvalidInputValueError extends ValueReaderError {

    private static final String MESSAGE = "Specified value (%s) is not a valid representation of type %s";

    /**
     * Instantiates the exception
     * @param value    the value being read
     * @param expectedType     the expected type against which the value is being evaluated
     */
    public InvalidInputValueError(String value, Class<?> expectedType) {
        this(String.format(MESSAGE, value, expectedType.getCanonicalName()));
    }

    /**
     * Instantiates the exception
     * @param value    the value being read
     * @param expectedType     the expected type against which the value is being evaluated
     */
    public InvalidInputValueError(String value, Class<?> expectedType, Throwable cause) {
        this(String.format(MESSAGE, value, expectedType.getCanonicalName()), cause);
    }

    /**
     * Instantiates the exception
     * @param message   the explanation for the error
     */
    public InvalidInputValueError(String message) {
        super(message);
    }

    /**
     * Instantiates the exception
     * @param message   the explanation for the error
     * @param cause     the root cause for this error
     */
    public InvalidInputValueError(String message, Throwable cause) {
        super(message, cause);
    }

}
