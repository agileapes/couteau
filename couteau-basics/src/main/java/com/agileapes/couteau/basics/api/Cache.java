/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.couteau.basics.api;

/**
 * This interface will encapsulate that which is the expected behaviour of a normal cache.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/4/13, 1:49 PM)
 */
public interface Cache<K, V> {

    /**
     * Determines whether or not an item with the specified key has been cached
     * @param key    the key to the item
     * @return {@code true} if this item has been cached
     */
    boolean contains(K key);

    /**
     * Stores this item in the cache. If another item with this key exists,
     * it will override that.
     * @param key      the key to the item
     * @param value    the item's value
     * @return {@code true} means that the item has been successfully written to the cache,
     * and can now be read from it.
     */
    boolean write(K key, V value);

    /**
     * Reads the specified item from the cache. If the item has not been cached, it will
     * return {@code null}, indicating a miss.
     * @param key    the key to the item
     * @return the cached value
     */
    V read(K key);

    /**
     * Removes the item with the given key from the cache
     * @param key    the key
     * @return {@code true} means that the item has been removed or it did not exist in the
     * first place. Getting a {@code false} means that the item is in the cache, but for some
     * reason could not be removed.
     */
    boolean remove(K key);

    /**
     * Removes any items accepted by the filter from the cache. This allows for invalidating entire
     * regions of cache en mass.
     */
    void remove(Filter<K> keyFilter);

    /**
     * Invalidates the cache, so that all items are removed. This is essentially the same
     * as removing all items from the cache one by one, but it is semantically faster.
     */
    void invalidate();

}
