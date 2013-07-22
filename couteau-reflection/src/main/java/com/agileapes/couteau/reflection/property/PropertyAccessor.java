package com.agileapes.couteau.reflection.property;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

/**
 * The PropertyAccessor encapsulates the definition of a property, regardless of the way through which
 * it can be accessed. This will allow a multitude of implementations to be developed, hiding the differences
 * and readily exposing only the functionality that does matter.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:24 PM)
 */
public interface PropertyAccessor<E> extends AnnotatedElement, Member {

    /**
     * @return the type of the property being wrapped.
     */
    Class<E> getPropertyType();

    /**
     * @return the generic type of the property being wrapped
     */
    Type getGenericPropertyType();

}
