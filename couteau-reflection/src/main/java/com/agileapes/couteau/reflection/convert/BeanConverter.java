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
