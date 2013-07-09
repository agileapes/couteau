package com.agileapes.couteau.reflection.property;

import com.agileapes.couteau.reflection.error.PropertyAccessException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:28 PM)
 */
public interface WritePropertyAccessor<E> extends PropertyAccessor<E> {

    void setPropertyValue(E value) throws PropertyAccessException;

}
