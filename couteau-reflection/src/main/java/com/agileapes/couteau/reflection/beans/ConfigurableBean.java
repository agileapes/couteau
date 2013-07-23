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

package com.agileapes.couteau.reflection.beans;

import com.agileapes.couteau.reflection.error.NoSuchPropertyException;
import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.error.PropertyTypeMismatchException;

/**
 * A ConfigurableBean is a bean whose properties can be manipulated from the outside.
 * Usually, the number of properties that can be manipulated in such a way is limited and
 * is specific to the bean implementing this interface.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:47 PM)
 */
public interface ConfigurableBean {

    /**
     * This method is used to set the value of available properties
     * @param propertyName     the name of the property
     * @param propertyValue    the new value of the property
     * @throws NoSuchPropertyException if this property does not exist
     * @throws PropertyAccessException if the property is immutable, or if errors occur while setting the value for the property
     * @throws PropertyTypeMismatchException if the type of the argument being passed is not valid
     */
    void setPropertyValue(String propertyName, Object propertyValue) throws NoSuchPropertyException, PropertyAccessException, PropertyTypeMismatchException;

}
