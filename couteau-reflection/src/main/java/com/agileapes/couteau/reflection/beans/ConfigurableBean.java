package com.agileapes.couteau.reflection.beans;

import com.agileapes.couteau.reflection.error.NoSuchPropertyException;
import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.error.PropertyTypeMismatchException;

/**
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
