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

package com.mmnaseri.couteau.basics.api.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a simple implementation of the cache interface, relying on {@link AbstractCache}
 * to provide the details of the workings.
 *
 * This implementation is <strong>not</strong> thread-safe.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/4/13, 3:31 PM)
 */
public class SimpleCache<K, V> extends AbstractCache<K, V> {

    private final Map<K,CacheData<V>> store = new HashMap<K, CacheData<V>>();

    public SimpleCache() {
    }

    public SimpleCache(int capacity) {
        super(capacity);
    }

    public SimpleCache(int capacity, RemovePolicy removePolicy) {
        super(capacity, removePolicy);
    }

    @Override
    protected Map<K, AbstractCache.CacheData<V>> getStore() {
        return store;
    }
}
