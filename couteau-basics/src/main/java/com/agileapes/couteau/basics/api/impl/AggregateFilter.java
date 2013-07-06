package com.agileapes.couteau.basics.api.impl;

import com.agileapes.couteau.basics.api.Filter;

/**
 * This filter will take in an array of filters and accept the input if any of the filters accepts the result.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 2:29 PM)
 */
public class AggregateFilter<E> implements Filter<E> {

    private final Filter<? super E>[] filters;

    public AggregateFilter(Filter<E>[] filters) {
        this.filters = filters;
    }

    @Override
    public boolean accepts(E item) throws Exception {
        for (Filter<? super E> filter : filters) {
            if (filter.accepts(item)) {
                return true;
            }
        }
        return false;
    }
}
