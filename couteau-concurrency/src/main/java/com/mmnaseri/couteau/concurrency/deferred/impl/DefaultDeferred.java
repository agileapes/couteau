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

package com.mmnaseri.couteau.concurrency.deferred.impl;

import com.mmnaseri.couteau.basics.api.Processor;
import com.mmnaseri.couteau.basics.api.Transformer;
import com.mmnaseri.couteau.concurrency.deferred.Deferred;
import com.mmnaseri.couteau.concurrency.deferred.DeferredStatus;
import com.mmnaseri.couteau.concurrency.deferred.Failure;
import com.mmnaseri.couteau.concurrency.deferred.Promise;
import com.mmnaseri.couteau.concurrency.error.PromiseResolutionError;
import com.mmnaseri.couteau.concurrency.error.PromiseTimeoutError;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/15 AD, 12:17)
 */
public class DefaultDeferred<E> implements Deferred<E> {

    private static class ResolvablePromise<E> implements Promise<E> {

        private final ConcurrentMap<DeferredStatus, List<Processor<?>>> callbacks;
        private DeferredStatus status;
        private E result;
        private Failure failure;

        private ResolvablePromise() {
            callbacks = new ConcurrentHashMap<DeferredStatus, List<Processor<?>>>();
            status = DeferredStatus.OPEN;
        }

        private void initCallbacks(DeferredStatus status) {
            callbacks.putIfAbsent(status, new CopyOnWriteArrayList<Processor<?>>());
        }

        private void addCallback(DeferredStatus status, Processor<?> callback) {
            initCallbacks(status);
            callbacks.get(status).add(callback);
        }

        @SuppressWarnings("unchecked")
        private void executeCallbacks() {
            if (!DeferredStatus.REJECTED.equals(status) && !DeferredStatus.RESOLVED.equals(status)) {
                throw new IllegalStateException("Trying to execute callbacks for an unsupported state");
            }
            initCallbacks(status);
            for (Processor processor : callbacks.get(status)) {
                if (status.equals(DeferredStatus.REJECTED)) {
                    processor.process(failure);
                } else {
                    processor.process(result);
                }
            }
            callbacks.clear();
        }

        @Override
        public <F> Promise<F> onSuccess(final Transformer<E, F> callback) {
            final ResolvablePromise<F> promise = new ResolvablePromise<F>();
            if (DeferredStatus.REJECTED.equals(status)) {
                return promise;
            }
            if (callback != null) {
                final Processor<E> processor = new Processor<E>() {
                    @Override
                    public void process(E result) {
                        final F transformed = callback.map(result);
                        promise.resolve(transformed);
                    }
                };
                if (DeferredStatus.RESOLVED.equals(status)) {
                    processor.process(result);
                    callbacks.clear();
                } else {
                    addCallback(DeferredStatus.RESOLVED, processor);
                    addCallback(DeferredStatus.REJECTED, new Processor<Object>() {
                        @Override
                        public void process(Object input) {
                            promise.reject((Failure) input);
                        }
                    });
                }
            }
            return promise;
        }

        @Override
        public Promise<E> onFailure(Processor<Failure> callback) {
            if (callback == null) {
                return this;
            }
            if (status.equals(DeferredStatus.OPEN)) {
                addCallback(DeferredStatus.REJECTED, callback);
            } else if (status.equals(DeferredStatus.REJECTED)) {
                callback.process(failure);
                callbacks.clear();
            }
            return this;
        }

        @Override
        public Promise<E> then(final Processor<E> successCallback) {
            onSuccess(new Transformer<E, Object>() {
                @Override
                public Object map(E input) {
                    successCallback.process(input);
                    return null;
                }
            });
            return this;
        }

        @Override
        public <F> Promise<F> then(Transformer<E, F> successCallback, Processor<Failure> failureCallback) {
            onFailure(failureCallback);
            return onSuccess(successCallback);
        }

        @Override
        public E resolve() throws PromiseResolutionError {
            return resolve(-1);
        }

        @Override
        public E resolve(long timeout) throws PromiseResolutionError, PromiseTimeoutError {
            return DeferredTask.wait(this, timeout);
        }

        @Override
        public <F> Promise<F> then(Transformer<E, F> successCallback) {
            return onSuccess(successCallback);
        }

        @Override
        public Promise<E> then(final Processor<E> successCallback, Processor<Failure> failureCallback) {
            onSuccess(new Transformer<E, Object>() {
                @Override
                public Object map(E input) {
                    successCallback.process(input);
                    return null;
                }
            });
            onFailure(failureCallback);
            return this;
        }

        private synchronized void resolve(E result) {
            if (DeferredStatus.OPEN.equals(status)) {
                status = DeferredStatus.RESOLVED;
                this.result = result;
                executeCallbacks();
            }
        }

        private synchronized void reject(Failure failure) {
            if (DeferredStatus.OPEN.equals(status)) {
                status = DeferredStatus.REJECTED;
                this.failure = failure;
                executeCallbacks();
            }
        }

    }

    private final ResolvablePromise<E> promise;

    public DefaultDeferred() {
        promise = new ResolvablePromise<E>();
    }

    @Override
    public Promise<E> getPromise() {
        return promise;
    }

    @Override
    public synchronized void resolve(E result) {
        promise.resolve(result);
    }

    @Override
    public void resolve() {
        resolve(null);
    }

    @Override
    public void reject(String reason) {
        reject(new ImmutableFailure(reason, null));
    }

    @Override
    public void reject(Throwable cause) {
        reject(new ImmutableFailure(null, cause));
    }

    @Override
    public void reject(String reason, Throwable cause) {
        reject(new ImmutableFailure(reason, cause));
    }

    @Override
    public synchronized void reject(Failure failure) {
        promise.reject(failure);
    }

    @Override
    public void reject() {
        reject(new ImmutableFailure(null, null));
    }

}
