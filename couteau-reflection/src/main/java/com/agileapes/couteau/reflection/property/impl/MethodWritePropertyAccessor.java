package com.agileapes.couteau.reflection.property.impl;

import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.property.WritePropertyAccessor;

import java.lang.reflect.Method;

/**
 * This accessor will update the value of the property through its associated setter method
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:36 PM)
 */
public class MethodWritePropertyAccessor<E> extends AbstractMethodPropertyAccessor<E> implements WritePropertyAccessor<E> {

    /**
     * Instantiates the accessor through the method
     * @param method    the method
     * @param target    the object being targeted
     */
    public MethodWritePropertyAccessor(Method method, Object target) {
        super(method, target);
    }

    /**
     * Updates the value of the property to the specified value
     * @param value    the new value of the property
     * @throws PropertyAccessException
     */
    @Override
    public void setPropertyValue(E value) throws PropertyAccessException {
        try {
            invoke(value);
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
        return (Class<E>) getMethod().getParameterTypes()[0];
    }

}
