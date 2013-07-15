package com.agileapes.couteau.reflection.convert;

import com.agileapes.couteau.reflection.error.BeanConversionException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 2:08 PM)
 */
public interface GenericBeanConverter<I, O> {

    O convert(I bean) throws BeanConversionException;

}
