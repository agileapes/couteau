package com.agileapes.couteau.reflection.property.impl;

import com.agileapes.couteau.reflection.property.PropertyDescriptor;

import java.lang.reflect.Type;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:33 AM)
 */
public class SimplePropertyDescriptor implements PropertyDescriptor {

    private final String name;
    private final Class<?> type;
    private final Object value;
    private final Type genericType;

    public SimplePropertyDescriptor(String name, Class<?> type, Type genericType) {
        this(name, type, genericType, null);
    }

    public SimplePropertyDescriptor(String name, Class<?> type, Type genericType, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.genericType = genericType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public Type getGenericType() {
        return genericType;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean isNull() {
        return value == null;
    }
}
