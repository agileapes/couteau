package com.agileapes.couteau.reflection.convert;

import com.agileapes.couteau.reflection.property.PropertyDescriptor;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:31 AM)
 */
public interface ConversionStrategy {

    ConversionDecision decide(PropertyDescriptor propertyDescriptor);

}
