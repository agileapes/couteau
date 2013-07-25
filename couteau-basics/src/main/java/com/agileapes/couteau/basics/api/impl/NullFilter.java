package com.agileapes.couteau.basics.api.impl;

import com.agileapes.couteau.basics.api.Filter;

/**
 * Accepts {@code null} items
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/23/13, 12:00 PM)
 */
public class NullFilter<E> implements Filter<E> {
    @Override
    public boolean accepts(E item) {
        return item == null;
    }
}
