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

package com.mmnaseri.couteau.concurrency.deferred;

/**
 * This interface will represent a deferred task that needs to be performed and whose results will become
 * available at some point in the future.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (14/6/15 AD, 12:09)
 */
public interface Deferred<E> {

    /**
     * @return a promise that will be {@link #resolve() resolved} at some point in the future
     */
    Promise<E> getPromise();

    /**
     * Resolves the promise for the given result.
     * @param result    the result which should be handed to the promise
     */
    void resolve(E result);

    /**
     * Resolves the promise using {@code null}.
     */
    void resolve();

    /**
     * Rejects the promise, making it unresolvable for the future.
     * @param reason    the reason for the rejection
     */
    void reject(String reason);

    /**
     * Rejects the promise, making it unresolvable for the future.
     * @param cause    the cause for this rejection
     */
    void reject(Throwable cause);

    /**
     * Rejects the promise, making it unresolvable for the future.
     * @param reason    the reason for the rejection
     * @param cause     the cause for this rejection
     */
    void reject(String reason, Throwable cause);

    /**
     * Rejects the promise for the given failure criteria
     * @param failure    the failure leading to this promise being rendered unresolvable
     */
    void reject(Failure failure);

    /**
     * Rejects the promise without specifying why. This means that the {@link Failure} object passed to rejection
     * handlers will return {@code null} or meaningless values when queried through properties.
     */
    void reject();

}
