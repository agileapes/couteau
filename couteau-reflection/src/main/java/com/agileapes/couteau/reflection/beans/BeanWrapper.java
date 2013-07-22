package com.agileapes.couteau.reflection.beans;

import com.agileapes.couteau.reflection.error.NoSuchPropertyException;

/**
 * This interface is the collision between the functionalities offered by {@link BeanAccessor}
 * and {@link ConfigurableBean}, while adding two additional methods:
 * <ul>
 *     <li>{@link #isWritable(String)}</li>
 *     <li>{@link #isReadable(String)}</li>
 * </ul>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:47 PM)
 */
public interface BeanWrapper<E> extends BeanAccessor<E>, ConfigurableBean {

    /**
     * Determines whether the given property can be written to or whether it is just
     * a read-only property.
     * @param propertyName    the name of the property
     * @return {@code true} if the property is writable
     * @throws NoSuchPropertyException
     */
    boolean isWritable(String propertyName) throws NoSuchPropertyException;

    /**
     * Determines whether the given property can be read from or whether read access
     * to this property is restricted.
     * @param propertyName    the name of the property
     * @return {@code true} if the property is readable
     * @throws NoSuchPropertyException
     */
    boolean isReadable(String propertyName) throws NoSuchPropertyException;

}
