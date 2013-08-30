package com.agileapes.couteau.context.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class implements the {@link com.agileapes.couteau.context.contract.Registry}
 * interface by using a concurrent map for the underlying registry storage.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/30, 14:44)
 */
public class ConcurrentRegistry<E> extends AbstractMapRegistry<E> {

    private final Map<String, E> map = new ConcurrentHashMap<String, E>();

    @Override
    protected Map<String, E> getMap() {
        return map;
    }

}
