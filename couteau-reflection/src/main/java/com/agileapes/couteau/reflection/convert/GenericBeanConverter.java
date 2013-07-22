package com.agileapes.couteau.reflection.convert;

import com.agileapes.couteau.reflection.error.BeanConversionException;

/**
 * This interface allows for the definition of converters with a single purpose, which know how to convert items
 * from one specific type to items of a predesignated type.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 2:08 PM)
 */
public interface GenericBeanConverter<I, O> {

    /**
     * Converts the input bean to an instance of the expected target type.
     * @param bean    the input bean
     * @return the converted object
     * @throws BeanConversionException
     */
    O convert(I bean) throws BeanConversionException;

}
