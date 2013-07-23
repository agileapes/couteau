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
