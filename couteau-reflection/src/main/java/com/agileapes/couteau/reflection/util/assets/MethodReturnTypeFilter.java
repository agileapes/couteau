package com.agileapes.couteau.reflection.util.assets;

import com.agileapes.couteau.basics.api.Filter;

import java.lang.reflect.Method;

/**
 * Filters methods by their return types
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 5:28 PM)
 */
public class MethodReturnTypeFilter implements Filter<Method> {

    private final Class<?> returnType;

    public MethodReturnTypeFilter(Class<?> returnType) {
        this.returnType = returnType;
    }

    @Override
    public boolean accepts(Method item) throws Exception {
        return returnType.isAssignableFrom(item.getReturnType());
    }

}
