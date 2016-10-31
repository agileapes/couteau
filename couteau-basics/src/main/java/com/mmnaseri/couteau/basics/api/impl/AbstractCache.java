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

import com.mmnaseri.couteau.basics.api.Cache;
import com.mmnaseri.couteau.basics.api.Filter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.mmnaseri.couteau.basics.collections.CollectionWrapper.with;

/**
 * This implementation allows for the cache to follow the contract as specified in
 * {@link Cache}. It additionally accepts a capacity and a remove policy.
 *
 * @see AbstractCache
 * @see RemovePolicy
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/4/13, 2:28 PM)
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {

    private static final int DEFAULT_ACCESS_RATE = 0;

    /**
     * The remove policy is a definition for how the cache should decide which items to lose
     * when it exceeds the capacity set for it.
     */
    public static enum RemovePolicy {
        /**
         * This policy means that the items that have been accessed the least will have to go.
         */
        LEAST_USED_FIRST,
        /**
         * This policy tells the cache to remove the oldest items first.
         */
        OLDEST_FIRST
    }

    /**
     * This class embodies the semantics of cached data, allowing to keep track of
     * how and when data are used in the cache.
     * @param <V>    the type of the data in the cache
     */
    protected static class CacheData<V> implements Comparable<CacheData<V>> {

        private V value;
        private long accessTime = 0;
        private long accessRate;

        private CacheData(V value) {
            this.value = value;
            this.accessRate = DEFAULT_ACCESS_RATE;
        }

        private V getValue() {
            accessRate ++;
            accessTime = new Date().getTime();
            return value;
        }

        private long getAccessRate() {
            return accessRate;
        }

        private long getAccessTime() {
            return accessTime;
        }

        @Override
        public int compareTo(CacheData<V> o) {
            return new Long(getAccessRate()).compareTo(o.getAccessRate());
        }

    }

    private final int capacity;
    private final RemovePolicy removePolicy;

    /**
     * This method will return the transform that will be used as the storage unit
     * for the cached data.
     * @return the storage
     */
    protected abstract Map<K, CacheData<V>> getStore();

    /**
     * Instantiates the cache without any capacity limits.
     */
    public AbstractCache() {
        this(Integer.MAX_VALUE);
    }

    /**
     * Instantiates the cache with the specified capacity, setting the
     * remove policy to {@link RemovePolicy#LEAST_USED_FIRST}
     * @param capacity    the capacity for the cache
     */
    public AbstractCache(int capacity) {
        this(capacity, RemovePolicy.LEAST_USED_FIRST);
    }

    /**
     * Instantiates the cache with the specified capacity and designates
     * the given remove policy
     * @param capacity        the capacity for the cache
     * @param removePolicy    the remove policy for when the cache is overflowing
     */
    public AbstractCache(int capacity, RemovePolicy removePolicy) {
        this.capacity = capacity;
        this.removePolicy = removePolicy;
    }

    /**
     * Determines whether or not an item with the specified key has been cached
     * @param key    the key to the item
     * @return {@code true} if this item has been cached
     */
    @Override
    public boolean contains(K key) {
        return getStore().containsKey(key);
    }

    /**
     * Will look for the first candidate to remove, based on the remove policy specified
     * for the cache.
     * @return the remove candidate item or {@code null} if non can be found.
     */
    private K getRemoveCandidate() {
        K key = null;
        long min = Integer.MAX_VALUE;
        for (Map.Entry<K, CacheData<V>> entry : getStore().entrySet()) {
            final CacheData<V> cacheData = entry.getValue();
            long value = removePolicy.equals(RemovePolicy.LEAST_USED_FIRST) ? cacheData.getAccessRate() : cacheData.getAccessTime();
            if (value < min) {
                min = value;
                key = entry.getKey();
            }
        }
        return key;
    }

    /**
     * Stores this item in the cache. If another item with this key exists,
     * it will override that.
     * @param key      the key to the item
     * @param value    the item's value
     * @return {@code true} means that the item has been successfully written to the cache,
     * and can now be read from it.
     */
    @Override
    public boolean write(K key, V value) {
        if (contains(key)) {
            remove(key);
        }
        while (getStore().size() > getCapacity()) {
            if (!remove(getRemoveCandidate())) {
                return false;
            }
        }
        getStore().put(key, new CacheData<V>(value));
        return contains(key);
    }

    /**
     * Reads the specified item from the cache. If the item has not been cached, it will
     * return {@code null}, indicating a miss.
     * @param key    the key to the item
     * @return the cached value
     */
    @Override
    public V read(K key) {
        if (!contains(key)) {
            return null;
        }
        return getStore().get(key).getValue();
    }

    /**
     * Removes the item with the given key from the cache
     * @param key    the key
     * @return {@code true} means that the item has been removed or it did not exist in the
     * first place. Getting a {@code false} means that the item is in the cache, but for some
     * reason could not be removed.
     */
    @Override
    public boolean remove(K key) {
        if (key == null) {
            return false;
        }
        getStore().remove(key);
        return !contains(key);
    }

    @Override
    public synchronized int remove(Filter<K> keyFilter) {
        //noinspection unchecked
        final List<K> removed = with(getStore().keySet()).keep(keyFilter).list();
        for (K key : removed) {
            remove(key);
        }
        return removed.size();
    }

    /**
     * Invalidates the cache, so that all items are removed. This is essentially the same
     * as removing all items from the cache one by one, but it is semantically faster.
     */
    @Override
    public void invalidate() {
        getStore().clear();
    }

    /**
     * @return the capacity originally set for the cache
     */
    public int getCapacity() {
        return capacity;
    }

}
