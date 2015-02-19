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

package com.agileapes.couteau.concurrency.deferred.impl;

import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.basics.api.Transformer;
import com.agileapes.couteau.concurrency.deferred.*;
import com.agileapes.couteau.concurrency.error.PromiseResolutionError;
import com.agileapes.couteau.concurrency.error.PromiseTimeoutError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is a utility class that should act as a sort of interface for creating deferred objects and augmented
 * promises.
 *
 * @see #all(java.util.List)
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/15 AD, 13:02)
 */
public final class DeferredTask {

    private DeferredTask() {}

    /**
     * @return a new deferred task that will not yield any results
     */
    public static Deferred<?> defer() {
        return new DefaultDeferred<Void>();
    }

    /**
     * Creates a deferred object which will yield results of the given type
     * @param resultType    the type of the result
     * @param <E>           the type parameter for the result
     * @return the deferred object
     */
    public static <E> Deferred<E> defer(Class<E> resultType) {
        return new DefaultDeferred<E>();
    }

    /**
     * Creates a deferred object capable of passing notifications to its promises
     * @param resultType          the type of the result
     * @param notificationType    the type of the notifications being passed around
     * @param <E>                 the type parameter for the result
     * @param <N>                 the type parameter for the notifications
     * @return the deferred object
     */
    public static <E, N extends Notification> NotifiableDeferred<E, N> defer(Class<E> resultType, Class<N> notificationType) {
        return new DefaultNotifiableDeferred<E, N>();
    }

    /**
     * This acts the same as {@link #all(java.util.List)}
     * @param promises    an array of all promises that should be wrapped
     * @return the single promise representing all promises
     */
    public static NotifiablePromise<TaskCompletionResult, TaskPerformedNotification> all(Promise<?>... promises) {
        return all(Arrays.asList(promises));
    }

    /**
     * <p>This method will take a list of promises and return a single, wrapper promise that will be resolved when
     * all promises have been resolved successfully. A notification of type {@link TaskPerformedNotification}
     * will be sent to the returned promise every time one of the tasks is carried out (whether successfully or not)
     * and in the end, the final promise will be resolved via {@link TaskCompletionResult} wherein the results
     * of the tasks will be made available in the same order as they were first scheduled via this method.</p>
     *
     * <p><strong>Note:</strong> the returned promise will be rejected even if one of the scheduled tasks has
     * failed, yielding the rejected task's index and failure criteria as the result of the failure via an
     * instance of {@link TaskFailureResult}.</p>
     *
     * <p>If you are scheduling a notification listener via one of {@link NotifiablePromise#then(Transformer, Processor, Processor)}
     * or {@link NotifiablePromise#then(Processor, Processor, Processor)} and the promises have already been resolved
     * or rejected, you will not receive any notifications. This is because notification listeners will not be registered
     * after a promise has been closed, and when attaching a rejection or resolution callback to a promise that has
     * already been resolved, it will be immediately fired. As such, the returned promise will be immediately closed,
     * without waiting for the notification listener to be registered.</p>
     * @param originalPromises    a list of all promises that should be wrapped
     * @return the single wrapper promise representing the input promises
     */
    public static NotifiablePromise<TaskCompletionResult, TaskPerformedNotification> all(final List<Promise<?>> originalPromises) {
        final List<Promise<?>> promises = new ArrayList<Promise<?>>(originalPromises);
        final NotifiableDeferred<TaskCompletionResult, TaskPerformedNotification> deferred = defer(TaskCompletionResult.class, TaskPerformedNotification.class);
        final ConcurrentMap<Integer, Object> results = new ConcurrentHashMap<Integer, Object>();
        final NotifiablePromise<TaskCompletionResult, TaskPerformedNotification> promise = deferred.getPromise();
        final AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < promises.size(); i++) {
            final int index = i;
            //noinspection unchecked
            promises.get(i).then(new Processor() {
                @Override
                public void process(Object result) {
                    results.putIfAbsent(index, result);
                    deferred.notify(new TaskPerformedNotification(index, result, null));
                    if (count.incrementAndGet() == originalPromises.size()) {
                        final ArrayList<Object> objects = new ArrayList<Object>();
                        for (int i = 0; i < promises.size(); i++) {
                            objects.add(results.get(i));
                        }
                        deferred.resolve(new TaskCompletionResult(objects));
                    }
                }
            }, new Processor<Failure>() {
                @Override
                public void process(Failure failure) {
                    deferred.notify(new TaskPerformedNotification(index, null, failure));
                    deferred.reject(new TaskFailureResult(index, failure));
                }
            });
        }
        return promise;
    }

    /**
     * Waits upon the resolution of a promise. This method blocks the execution of the current thread until the
     * promise is resolved, it is rejected, or a timeout is reached.
     * @param promise    the promise to wait upon
     * @param timeout    the timeout in milliseconds. A negative value for the timeout will mean that the promise
     *                   will not be timed out at all.
     * @param <E>        the type of the value that is expected to be returned through the resolution of the promise.
     * @return the value with which the promise has been resolved.
     * @throws PromiseResolutionError should the promise be rejected, or the current thread be interrupted while waiting
     * upon the promise to resolve
     * @throws PromiseTimeoutError should the promise take more than the specified timeout (in milliseconds) to resolve
     */
    public static <E> E wait(Promise<E> promise, long timeout) throws PromiseResolutionError, PromiseTimeoutError {
        final AtomicReference<E> reference = new AtomicReference<E>();
        final AtomicBoolean resolved = new AtomicBoolean(false);
        final AtomicReference<Failure> rejected = new AtomicReference<Failure>();
        promise.onSuccess(new Transformer<E, Object>() {
            @Override
            public Object map(E input) {
                reference.set(input);
                resolved.set(true);
                return null;
            }
        });
        promise.onFailure(new Processor<Failure>() {
            @Override
            public void process(Failure input) {
                rejected.set(input);
            }
        });
        long startTime = System.currentTimeMillis();
        while (!resolved.get() && rejected.get() == null) {
            if (timeout > 0 && System.currentTimeMillis() - startTime > timeout) {
                throw new PromiseTimeoutError(timeout);
            }
            if (rejected.get() != null) {
                throw new PromiseResolutionError("Promise was rejected", rejected.get().getCause());
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new PromiseResolutionError("Failed to wait for promise resolution due to interruption", e);
            }
        }
        return reference.get();
    }

}
