package com.agileapes.couteau.reflection.property.impl;

import com.agileapes.couteau.reflection.property.PropertyAccessor;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:25 PM)
 */
public abstract class AbstractPropertyAccessor<E> implements PropertyAccessor<E> {

    private final Class<E> propertyType;
    private final String propertyName;

    public AbstractPropertyAccessor(String propertyName, Class<E> propertyType) {
        this.propertyType = propertyType;
        this.propertyName = propertyName;
    }

    @Override
    public Class<E> getPropertyType() {
        return propertyType;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }
}
