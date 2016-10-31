/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mmnaseri.couteau.reflection.beans;

import com.mmnaseri.couteau.reflection.error.BeanInstantiationException;

/**
 * The bean initializer interfaces the process of initializing a bean from a specific type
 * using a given constructor signature.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/4/13, 6:54 PM)
 */
public interface BeanInitializer {

    /**
     * <p>This method will initialize a bean of the given type using the specified initialization
     * arguments.</p>
     *
     * <p>Any errors occurring during this process will be reflected as a BeanInstantiationException
     * to simplify handing exceptions for bean initialization.</p>
     * @param type             the type of the bean
     * @param argumentTypes    the types of the arguments being passed
     * @param arguments        the arguments used for initialization of the bean
     * @param <E>              the type of the bean
     * @return an instance of the bean in question
     * @throws BeanInstantiationException
     */
    <E> E initialize(Class<E> type, Class[] argumentTypes, Object... arguments) throws BeanInstantiationException;

}
