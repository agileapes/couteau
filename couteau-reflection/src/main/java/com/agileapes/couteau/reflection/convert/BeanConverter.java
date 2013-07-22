package com.agileapes.couteau.reflection.convert;

import com.agileapes.couteau.reflection.error.BeanConversionException;

/**
 * This interface defines the contract for a bean converter that supports conversion of beans from
 * an arbitrary type to an arbitrary target type
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 6:47 AM)
 */
public interface BeanConverter {

    /**
     * This method will convert the input bean into an instance of the specified target type.
     * @param bean          the input bean
     * @param targetType    target type
     * @param <I>           the input type
     * @param <O>           the output type
     * @return converted bean
     * @throws BeanConversionException
     */
    <I, O> O convert(I bean, Class<O> targetType) throws BeanConversionException;

}
