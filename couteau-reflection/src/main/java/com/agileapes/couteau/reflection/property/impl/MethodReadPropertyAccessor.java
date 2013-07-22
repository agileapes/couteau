package com.agileapes.couteau.reflection.property.impl;

import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.property.ReadPropertyAccessor;

import java.lang.reflect.Method;

/**
 * This class reads the value of a property through the associated getter method
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:29 PM)
 */
public class MethodReadPropertyAccessor<E> extends AbstractMethodPropertyAccessor<E> implements ReadPropertyAccessor<E> {

    /**
     * Instantiates the accessor through the method
     * @param method    the method
     * @param target    the object being targeted
     */
    public MethodReadPropertyAccessor(Method method, Object target) {
        super(method, target);
    }

    /**
     * Reads and returns the current value of the property
     * @return the value of the property
     * @throws PropertyAccessException
     */
    @Override
    public E getPropertyValue() throws PropertyAccessException {
        try {
            //noinspection unchecked
            return (E) invoke();
        } catch (Exception e) {
            throw new PropertyAccessException(getTargetType(), getName(), e);
        }
    }

    /**
     * @return the type of the property being wrapped.
     */
    @Override
    public Class<E> getPropertyType() {
        //noinspection unchecked
        return (Class<E>) getMethod().getReturnType();
    }

}
