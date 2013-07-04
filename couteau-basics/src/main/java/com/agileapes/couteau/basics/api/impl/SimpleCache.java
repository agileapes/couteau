package com.agileapes.couteau.basics.api.impl;

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

    @Override
    protected Map<K, AbstractCache.CacheData<V>> getStore() {
        return store;
    }
}
