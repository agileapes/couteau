/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
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
 * This shows an error within a registry operation that is irreversible and thus fatal to the
 * registry. Such errors must be handled by either shutting the application down, or reviving the
 * registry anew.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
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
