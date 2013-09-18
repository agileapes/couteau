package com.agileapes.couteau.basics.api.impl;

import com.agileapes.couteau.basics.api.Transformer;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/18, 18:28)
 */
public class CastingTransformer<I, O> implements Transformer<I, O> {

    private final Class<O> targetType;

    public CastingTransformer(Class<O> targetType) {
        this.targetType = targetType;
    }

    @Override
    public O map(I input) {
        return targetType.cast(input);
    }

}
