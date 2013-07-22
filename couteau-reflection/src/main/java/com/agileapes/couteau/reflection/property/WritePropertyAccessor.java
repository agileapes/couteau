package com.agileapes.couteau.reflection.property;

import com.agileapes.couteau.reflection.error.PropertyAccessException;

/**
 * This interface allows for updating the value of a property through the underlying accessor
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:28 PM)
 */
public interface WritePropertyAccessor<E> extends PropertyAccessor<E> {

    /**
     * Updates the value of the property to the specified value
     * @param value    the new value of the property
     * @throws PropertyAccessException
     */
    void setPropertyValue(E value) throws PropertyAccessException;

}
