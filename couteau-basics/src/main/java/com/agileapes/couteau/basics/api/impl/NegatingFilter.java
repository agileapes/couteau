package com.agileapes.couteau.basics.api.impl;

import com.agileapes.couteau.basics.api.Filter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/10/3, 14:08)
 */
public class NegatingFilter<I> implements Filter<I> {

    private final Filter<? super I> filter;

    public NegatingFilter(Filter<? super I> filter) {
        this.filter = filter;
    }

    @Override
    public boolean accepts(I item) {
        return !filter.accepts(item);
    }

}
