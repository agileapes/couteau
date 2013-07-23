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

package com.agileapes.couteau.reflection.convert.impl;

import com.agileapes.couteau.reflection.beans.BeanAccessor;
import com.agileapes.couteau.reflection.beans.BeanAccessorFactory;
import com.agileapes.couteau.reflection.beans.BeanWrapper;
import com.agileapes.couteau.reflection.beans.BeanWrapperFactory;
import com.agileapes.couteau.reflection.convert.ConversionDecision;
import com.agileapes.couteau.reflection.convert.ConversionStrategy;
import com.agileapes.couteau.reflection.error.*;
import com.agileapes.couteau.reflection.property.impl.SimplePropertyDescriptor;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:02 AM)
 */
public class DefaultBeanConverter extends AbstractCachingBeanConverter {

    /**
     * Instantiates the converter with the default strategy ({@link DefaultConversionStrategy})
     * @see #DefaultBeanConverter(ConversionStrategy)
     */
    public DefaultBeanConverter() {
        super();
    }

    /**
     * Instantiates the converter using the given strategy, defaulting to a method bean wrapper factory
     * ({@link com.agileapes.couteau.reflection.beans.impl.MethodBeanWrapperFactory})
     * @param conversionStrategy    the conversion strategy to be used throughout the conversion
     * @see #DefaultBeanConverter(BeanWrapperFactory, ConversionStrategy)
     */
    public DefaultBeanConverter(ConversionStrategy conversionStrategy) {
        super(conversionStrategy);
    }

    /**
     * Instantiates the converter using the given wrapper factory and the given strategy. In this case, the
     * wrapper factory will act as the accessor factory, as well.
     * @param wrapperFactory        the wrapper factory
     * @param conversionStrategy    the conversion strategy to be used throughout the conversion
     * @see #DefaultBeanConverter(BeanAccessorFactory, BeanWrapperFactory, ConversionStrategy)
     */
    public DefaultBeanConverter(BeanWrapperFactory wrapperFactory, ConversionStrategy conversionStrategy) {
        super(wrapperFactory, conversionStrategy);
    }

    /**
     * Instantiates the converter using the given parameters.
     * @param accessorFactory    the accessor factory for input objects
     * @param wrapperFactory     the wrapper factory for output objects
     * @param conversionStrategy the conversion strategy to be used throughout the conversion
     */
    public DefaultBeanConverter(BeanAccessorFactory accessorFactory, BeanWrapperFactory wrapperFactory, ConversionStrategy conversionStrategy) {
        super(accessorFactory, wrapperFactory, conversionStrategy);
    }

    /**
     * This method is expected to carry out the task of conversion by converting values from
     * properties read from the source object into the destination object
     * @param source    the accessor for the input bean
     * @param target    the wrapper for the target bean
     * @throws BeanConversionException
     */
    @Override
    protected void doConvert(BeanAccessor<?> source, BeanWrapper<?> target) throws BeanConversionException {
        for (String property : source.getPropertyNames()) {
            final Object propertyValue;
            try {
                propertyValue = source.getPropertyValue(property);
            } catch (Exception e) {
                throw new FatalBeanConversionException("Failed to read property value: " + source.getBeanType().getCanonicalName().concat(".").concat(property), e);
            }
            if (!target.hasProperty(property)) {
                continue;
            }
            try {
                if (!target.isWritable(property)) {
                    continue;
                }
            } catch (NoSuchPropertyException ignored) {
            }
            Class<?> propertyType = null;
            try {
                propertyType = target.getPropertyType(property);
            } catch (NoSuchPropertyException ignored) {
            }
            if (propertyType == null) {
                try {
                    propertyType = source.getPropertyType(property);
                } catch (NoSuchPropertyException ignored) {
                }
            }
            Object convertedValue;
            SimplePropertyDescriptor targetPropertyDescriptor = null;
            SimplePropertyDescriptor sourcePropertyDescriptor = null;
            try {
                targetPropertyDescriptor = new SimplePropertyDescriptor(property, propertyType, target.getGenericPropertyType(property), propertyValue);
                sourcePropertyDescriptor = new SimplePropertyDescriptor(property, source.getPropertyType(property), source.getGenericPropertyType(property), propertyValue);
            } catch (NoSuchPropertyException ignored) {
            }
            final ConversionDecision decision = getConversionStrategy().decide(sourcePropertyDescriptor);
            if (decision.equals(ConversionDecision.IGNORE)) {
                continue;
            } else {
                convertedValue = convertProperty(targetPropertyDescriptor, decision);
            }
            try {
                target.setPropertyValue(property, convertedValue);
            } catch (NoSuchPropertyException ignored) {
            } catch (PropertyAccessException e) {
                throw new FatalBeanConversionException("Failed to access property " + target.getBeanType().getCanonicalName().concat(".").concat(property));
            } catch (PropertyTypeMismatchException ignored) {
            }
        }
    }

}
