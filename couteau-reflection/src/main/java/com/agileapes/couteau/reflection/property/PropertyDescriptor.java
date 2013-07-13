package com.agileapes.couteau.reflection.property;

import java.lang.reflect.Type;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:32 AM)
 */
public interface PropertyDescriptor {

    String getName();

    Class<?> getType();

    Type getGenericType();

    Object getValue();

    boolean isNull();

}
