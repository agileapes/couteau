package com.agileapes.couteau.context.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a simple registry that is <strong>not</strong> designed to be
 * thread-safe.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/30, 15:55)
 */
public class SimpleRegistry<E> extends AbstractMapRegistry<E> {

    private final Map<String, E> map = new HashMap<String, E>();

    @Override
    protected Map<String, E> getMap() {
        return map;
    }
}
