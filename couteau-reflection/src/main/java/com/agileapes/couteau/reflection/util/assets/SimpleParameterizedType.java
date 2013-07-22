package com.agileapes.couteau.reflection.util.assets;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This is a simple implementation for use when a parameterized type is required.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/15/13, 6:38 PM)
 */
public class SimpleParameterizedType implements ParameterizedType {

    private final Type rawType;
    private final Type ownerType;
    private final Type[] parameters;

    public SimpleParameterizedType(Type rawType, Type ownerType, Type[] parameters) {
        this.rawType = rawType;
        this.ownerType = ownerType;
        this.parameters = parameters;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return parameters;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }
}
