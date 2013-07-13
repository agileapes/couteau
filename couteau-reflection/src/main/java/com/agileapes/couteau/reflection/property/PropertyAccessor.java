package com.agileapes.couteau.reflection.property;

import java.lang.reflect.Type;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:24 PM)
 */
public interface PropertyAccessor<E> {

    Class<E> getPropertyType();

    Type getGenericPropertyType();

    String getPropertyName();

}
