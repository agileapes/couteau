package com.agileapes.couteau.reflection.property.impl;

import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.property.WritePropertyAccessor;

import java.lang.reflect.Field;

/**
 * This class allows for updating the value of a property through its associated field
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:42 PM)
 */
public class FieldWritePropertyAccessor<E> extends AbstractFieldPropertyAccessor<E> implements WritePropertyAccessor<E> {

    /**
     * Instantiates the accessor, while wrapping the field and taking in the invocation target
     * @param field     the field
     * @param target    the target
     */
    public FieldWritePropertyAccessor(Field field, Object target) {
        super(field, target);
    }

    /**
     * Updates the value of the property to the specified value
     * @param value    the new value of the property
     * @throws PropertyAccessException
     */
    @Override
    public void setPropertyValue(E value) throws PropertyAccessException {
        try {
            set(value);
        } catch (Exception e) {
            throw new PropertyAccessException(getTargetType(), getName(), e);
        }
    }

}
