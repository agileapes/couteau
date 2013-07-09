package com.agileapes.couteau.reflection.property;

import com.agileapes.couteau.reflection.error.PropertyAccessException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:27 PM)
 */
public interface ReadPropertyAccessor<E> extends PropertyAccessor<E> {

    E getPropertyValue() throws PropertyAccessException;

}
