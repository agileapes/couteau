package com.agileapes.couteau.reflection.property.impl;

import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.property.ReadPropertyAccessor;

import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:29 PM)
 */
public class MethodReadPropertyAccessor<E> extends AbstractMethodPropertyAccessor<E> implements ReadPropertyAccessor<E> {

    public MethodReadPropertyAccessor(String propertyName, Class<E> propertyType, Method method, Object target) {
        super(propertyName, propertyType, method, target);
    }

    @Override
    public E getPropertyValue() throws PropertyAccessException {
        try {
            //noinspection unchecked
            return (E) invoke();
        } catch (Exception e) {
            throw new PropertyAccessException(getTargetType(), getPropertyName(), e);
        }
    }

}
