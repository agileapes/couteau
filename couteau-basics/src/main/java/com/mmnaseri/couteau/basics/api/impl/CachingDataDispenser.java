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

package com.mmnaseri.couteau.basics.api.impl;

import com.mmnaseri.couteau.basics.api.Cache;
import com.mmnaseri.couteau.basics.api.Filter;

/**
 * This class denotes a data dispenser that is capable of caching results in case of a miss
 * and returning those cached values for the future.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (8/22/13, 12:40 PM)
 */
public abstract class CachingDataDispenser<K, V> implements Cache<K, V> {

    protected abstract Cache<K, V> getCache();

    protected abstract V produce(K key);

    @Override
    public boolean contains(K key) {
        return getCache().contains(key);
    }

    @Override
    public boolean write(K key, V value) {
        return getCache().write(key, value);
    }

    @Override
    public V read(K key) {
        if (!contains(key)) {
            final V product = produce(key);
            if (product != null) {
                write(key, product);
            } else {
                return null;
            }
        }
        return getCache().read(key);
    }

    @Override
    public boolean remove(K key) {
        return getCache().remove(key);
    }

    @Override
    public int remove(Filter<K> keyFilter) {
        return getCache().remove(keyFilter);
    }

    @Override
    public void invalidate() {
        getCache().invalidate();
    }

}
