package com.agileapes.couteau.reflection.convert;

import com.agileapes.couteau.reflection.error.BeanConversionException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 6:47 AM)
 */
public interface BeanConverter {

    <I, O> O convert(I bean, Class<O> targetType) throws BeanConversionException;

}
