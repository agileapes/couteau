package com.agileapes.couteau.reflection.convert;

/**
 * Beans implementing ConversionTarget are beans that want to be aware of the bean they are representing.
 * This means that these beans will have access to the bean they are being converted from.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/15/13, 5:52 PM)
 */
public interface ConversionTarget {

    /**
     * Sets the original bean
     * @param bean    the been being converted
     */
    void setBean(Object bean);

}
