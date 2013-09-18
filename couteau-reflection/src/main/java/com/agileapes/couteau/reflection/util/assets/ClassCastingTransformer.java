package com.agileapes.couteau.reflection.util.assets;

import com.agileapes.couteau.basics.api.Transformer;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/17, 18:43)
 */
public class ClassCastingTransformer<T> implements Transformer<Class<?>, Class<? extends T>> {

    private final Class<T> superType;

    public ClassCastingTransformer(Class<T> superType) {
        this.superType = superType;
    }

    @Override
    public Class<? extends T> map(Class<?> input) {
        return input.asSubclass(superType);
    }
}
