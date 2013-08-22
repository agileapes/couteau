package com.agileapes.couteau.basics.api.impl;

import com.agileapes.couteau.basics.api.Cache;

/**
 * This is an extension to {@link CachingDataDispenser} that designates a simple (non-thread-safe)
 * cache as the data store for items.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/22/13, 12:45 PM)
 */
public abstract class SimpleDataDispenser<K, V> extends CachingDataDispenser<K, V> {

    private final Cache<K,V> cache = new SimpleCache<K, V>();

    @Override
    protected Cache<K, V> getCache() {
        return cache;
    }

}
