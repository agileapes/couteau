package com.agileapes.couteau.basics.api.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a thread-safe implementation of the cache interface.
 *
 * This implementation relies on {@link AbstractCache} to provide the details of how the cache
 * should behave.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/4/13, 3:32 PM)
 */
public class ConcurrentCache<K, V> extends AbstractCache<K, V> {

    private final Map<K,CacheData<V>> store = new ConcurrentHashMap<K, CacheData<V>>();

    @Override
    protected Map<K, CacheData<V>> getStore() {
        return store;
    }

}
