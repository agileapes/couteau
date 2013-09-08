package com.agileapes.couteau.basics.api.impl;

import com.agileapes.couteau.basics.api.Filter;

/**
 * This is a filter that will help with filtering through a collection
 * of filters, by accepting all filters that accept a certain item.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/8, 19:43)
 */
public class MirrorFilter<E> implements Filter<Filter<E>> {

    private final E item;

    public MirrorFilter(E item) {
        this.item = item;
    }

    @Override
    public boolean accepts(Filter<E> filter) {
        return filter.accepts(item);
    }

}
