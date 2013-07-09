package com.agileapes.couteau.reflection.property.impl;

import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.property.WritePropertyAccessor;

import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:36 PM)
 */
public class MethodWritePropertyAccessor<E> extends AbstractMethodPropertyAccessor<E> implements WritePropertyAccessor<E> {

    public MethodWritePropertyAccessor(String propertyName, Class<E> propertyType, Method method, Object target) {
        super(propertyName, propertyType, method, target);
    }

    @Override
    public void setPropertyValue(E value) throws PropertyAccessException {
        try {
            invoke(value);
        } catch (Exception e) {
            throw new PropertyAccessException(getTargetType(), getPropertyName(), e);
        }
    }

}
