package com.agileapes.couteau.reflection.convert.impl;

import com.agileapes.couteau.reflection.convert.ConversionDecision;
import com.agileapes.couteau.reflection.convert.ConversionStrategy;
import com.agileapes.couteau.reflection.property.PropertyDescriptor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:36 AM)
 */
public class DefaultConversionStrategy implements ConversionStrategy {

    @Override
    public ConversionDecision decide(PropertyDescriptor propertyDescriptor) {
        final Class<?> propertyType = ReflectionUtils.getComponentType(ReflectionUtils.mapType(propertyDescriptor.getType()));
        if (propertyDescriptor.getName().equals("class") || propertyType.getCanonicalName().matches("java\\.lang\\.[^\\.]+")) {
            return ConversionDecision.PASS;
        }
        return ConversionDecision.CONVERT;
    }

}
