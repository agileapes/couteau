package com.agileapes.couteau.reflection.util.assets;

import com.agileapes.couteau.basics.api.Filter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/17, 18:42)
 */
public class AssignableTypeFilter implements Filter<Class<?>> {

    private final Class<?> superType;

    public AssignableTypeFilter(Class<?> superType) {
        this.superType = superType;
    }

    @Override
    public boolean accepts(Class<?> item) {
        return superType.isAssignableFrom(item);
    }
}
