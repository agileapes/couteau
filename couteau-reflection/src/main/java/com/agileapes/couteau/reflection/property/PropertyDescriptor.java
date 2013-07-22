package com.agileapes.couteau.reflection.property;

import java.lang.reflect.Type;

/**
 * This interface will help describe a property. All of its signature features will be included in this
 * interface.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:32 AM)
 */
public interface PropertyDescriptor {

    /**
     * @return the name of the property
     */
    String getName();

    /**
     * @return the type of the property
     */
    Class<?> getType();

    /**
     * @return the generic type of the property
     */
    Type getGenericType();

    /**
     * @return the value of the property
     */
    Object getValue();

    /**
     * Determines whether this property has a value of {@code null}
     * @return {@code true} if the value of the property has not been set or if it is set to {@code null}
     */
    boolean isNull();

}
