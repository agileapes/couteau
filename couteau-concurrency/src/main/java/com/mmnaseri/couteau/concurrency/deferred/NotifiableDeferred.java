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

package com.mmnaseri.couteau.concurrency.deferred;

/**
 * This interface builds on top of {@link Deferred} to enable notifying promises via {@link #notify(Notification)}
 *
 * @see com.mmnaseri.couteau.concurrency.deferred.NotifiablePromise
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (14/6/15 AD, 12:12)
 */
public interface NotifiableDeferred<E, N extends Notification> extends Deferred<E> {

    /**
     * Will notify the promise with a notification
     * @param notification    the notification to pass to the promise
     */
    void notify(N notification);

    /**
     * @return the promise object uniquely associated with this deferred object
     */
    NotifiablePromise<E, N> getPromise();

}
