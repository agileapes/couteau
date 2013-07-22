package com.agileapes.couteau.reflection.convert;

import com.agileapes.couteau.reflection.property.PropertyDescriptor;

/**
 * A conversion strategy is a central way for abstracting the process of deciding which items should be
 * converted and which items do not need that.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:31 AM)
 */
public interface ConversionStrategy {

    /**
     * Will make a decision based on the input property descriptor.
     * @param propertyDescriptor    the property descriptor for which a conversion decision is needed
     * @return the decision that determines which action should be taken
     */
    ConversionDecision decide(PropertyDescriptor propertyDescriptor);

}
