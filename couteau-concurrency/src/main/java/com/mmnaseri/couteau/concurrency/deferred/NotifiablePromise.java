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

import com.mmnaseri.couteau.basics.api.Processor;
import com.mmnaseri.couteau.basics.api.Transformer;

/**
 * This interface builds on {@link Promise} to allow for sending notifications to listener callbacks.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (14/6/15 AD, 12:16)
 */
public interface NotifiablePromise<E, N extends Notification> extends Promise<E> {

    /**
     * Schedules a callback that will be executed every time a notification is sent via the deferred
     * object's {@link com.mmnaseri.couteau.concurrency.deferred.NotifiableDeferred#notify(Notification)}
     * method.
     * @param callback    the callback to execute with the notification, if {@code null} it will be ignored
     * @return the promise itself
     */
    NotifiablePromise<E, N> onNotification(Processor<N> callback);

    /**
     * Schedules a callback that will be executed when the promise is fulfilled.
     * @param callback    the callback to be executed, if {@code null} it will be ignored
     * @param <F>         the type of the value returned by the callback
     * @return a promise that will be resolved with the value returned from this callback
     */
    <F> NotifiablePromise<F, N> onSuccess(Transformer<E, F> callback);

    /**
     * Schedules a callback that will be executed in case of a failure for the promise
     * @param callback    the callback to be executed, if {@code null} it will be ignored
     * @return the promise itself
     */
    NotifiablePromise<E, N> onFailure(Processor<Failure> callback);

    /**
     * The same as {@link #onSuccess(Transformer)}
     * @param successCallback    the callback to be executed in case of success
     * @return the promise itself
     */
    NotifiablePromise<E, N> then(Processor<E> successCallback);

    /**
     * Schedules two callbacks, one for when the promise is resolved successfully, and one for when it is
     * resolved with a rejection
     * @param successCallback    the callback to be executed in case of success
     * @param failureCallback    the callback to be executed in case of failure
     * @return the promise itself
     */
    NotifiablePromise<E, N> then(Processor<E> successCallback, Processor<Failure> failureCallback);

    /**
     * Schedules three callbacks, one for when the promise is resolved successfully, one for when it is
     * resolved with a rejection, and one which will be called every time a notification is sent to the
     * promise
     * @param successCallback       the callback to be executed in case of success
     * @param failureCallback       the callback to be executed in case of failure
     * @param notificationCallback  the callback which will receive notifications
     * @return a promise linked to this promise which will be resolved with the value returned from the callback
     */
    NotifiablePromise<E, N> then(Processor<E> successCallback, Processor<Failure> failureCallback, Processor<N> notificationCallback);

    /**
     * The same as {@link #onSuccess(Transformer)}
     * @param successCallback    the callback to be executed in case of success
     * @param <F>                the type of the value returned by the callback
     * @return a promise linked to this promise which will be resolved with the value returned from the callback
     */
    <F> NotifiablePromise<F, N> then(Transformer<E, F> successCallback);

    /**
     * Schedules two callbacks, one for when the promise is resolved successfully, and one for when it is
     * resolved with a rejection
     * @param successCallback    the callback to be executed in case of success
     * @param failureCallback    the callback to be executed in case of failure
     * @param <F>                the type of the value returned by the callback
     * @return a promise linked to this promise which will be resolved with the value returned from the callback
     */
    <F> NotifiablePromise<F, N> then(Transformer<E, F> successCallback, Processor<Failure> failureCallback);

    /**
     * Schedules three callbacks, one for when the promise is resolved successfully, one for when it is
     * resolved with a rejection, and one which will be called every time a notification is sent to the
     * promise
     * @param successCallback       the callback to be executed in case of success
     * @param failureCallback       the callback to be executed in case of failure
     * @param notificationCallback  the callback which will receive notifications
     * @return a promise linked to this promise which will be resolved with the value returned from the callback
     */
    <F> NotifiablePromise<F, N> then(Transformer<E, F> successCallback, Processor<Failure> failureCallback, Processor<N> notificationCallback);

}
