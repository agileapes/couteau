package com.agileapes.couteau.reflection.property.impl;

import com.agileapes.couteau.reflection.property.PropertyDescriptor;

import java.lang.reflect.Type;

/**
 * This is a simple, immutable property descriptor.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:33 AM)
 */
public class SimplePropertyDescriptor implements PropertyDescriptor {

    private final String name;
    private final Class<?> type;
    private final Object value;
    private final Type genericType;

    /**
     * Instantiates the descriptor, defaulting {@link #value} to {@code null}
     * @param name           the name of the property
     * @param type           the type of the property
     * @param genericType    the generic type of the property
     */
    public SimplePropertyDescriptor(String name, Class<?> type, Type genericType) {
        this(name, type, genericType, null);
    }

    /**
     * Instantiates the descriptor
     * @param name           the name of the property
     * @param type           the type of the property
     * @param genericType    the generic type of the property
     * @param value          the value of the property
     */
    public SimplePropertyDescriptor(String name, Class<?> type, Type genericType, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.genericType = genericType;
    }

    /**
     * @return the name of the property
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the type of the property
     */
    @Override
    public Class<?> getType() {
        return type;
    }

    /**
     * @return the generic type of the property
     */
    @Override
    public Type getGenericType() {
        return genericType;
    }

    /**
     * @return the value of the property
     */
    @Override
    public Object getValue() {
        return value;
    }

    /**
     * Determines whether this property has a value of {@code null}
     * @return {@code true} if the value of the property has not been set or if it is set to {@code null}
     */
    @Override
    public boolean isNull() {
        return value == null;
    }

}
