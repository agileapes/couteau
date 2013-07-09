package com.agileapes.couteau.reflection.property.impl;

import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.property.WritePropertyAccessor;

import java.lang.reflect.Field;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:42 PM)
 */
public class FieldWritePropertyAccessor<E> extends AbstractFieldPropertyAccessor<E> implements WritePropertyAccessor<E> {

    public FieldWritePropertyAccessor(String propertyName, Class<E> propertyType, Field field, Object target) {
        super(propertyName, propertyType, field, target);
    }

    @Override
    public void setPropertyValue(E value) throws PropertyAccessException {
        try {
            set(value);
        } catch (Exception e) {
            throw new PropertyAccessException(getTargetType(), getPropertyName(), e);
        }
    }

}
