/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

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
