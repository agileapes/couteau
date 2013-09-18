package com.agileapes.couteau.basics.api.impl;

import com.agileapes.couteau.basics.api.Filter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/18, 11:59)
 */
public class EqualityFilter<I> implements Filter<I> {

    private final I target;

    public EqualityFilter(I target) {
        this.target = target;
    }

    @Override
    public boolean accepts(I item) {
        return target.equals(item);
    }

}
