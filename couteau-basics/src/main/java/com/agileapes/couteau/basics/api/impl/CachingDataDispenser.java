package com.agileapes.couteau.basics.api.impl;

import com.agileapes.couteau.basics.api.Cache;

/**
 * This class denotes a data dispenser that is capable of caching results in case of a miss
 * and returning those cached values for the future.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
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
    public void invalidate() {
        getCache().invalidate();
    }

}
