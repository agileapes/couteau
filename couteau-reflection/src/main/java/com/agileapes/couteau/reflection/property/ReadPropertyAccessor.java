package com.agileapes.couteau.reflection.property;

import com.agileapes.couteau.reflection.error.PropertyAccessException;

/**
 * This interface enables you to read property values through the accessor underlying the implementation
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:27 PM)
 */
public interface ReadPropertyAccessor<E> extends PropertyAccessor<E> {

    /**
     * Reads and returns the current value of the property
     * @return the value of the property
     * @throws PropertyAccessException
     */
    E getPropertyValue() throws PropertyAccessException;

}
