package com.agileapes.couteau.reflection.property.impl;

import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.property.ReadPropertyAccessor;

import java.lang.reflect.Field;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:40 PM)
 */
public class FieldReadPropertyAccessor<E> extends AbstractFieldPropertyAccessor<E> implements ReadPropertyAccessor<E> {

    public FieldReadPropertyAccessor(String propertyName, Class<E> propertyType, Field field, Object target) {
        super(propertyName, propertyType, field, target);
    }

    @Override
    public E getPropertyValue() throws PropertyAccessException {
        try {
            //noinspection unchecked
            return (E) get();
        } catch (Exception e) {
            throw new PropertyAccessException(getTargetType(), getPropertyName(), e);
        }
    }

}
