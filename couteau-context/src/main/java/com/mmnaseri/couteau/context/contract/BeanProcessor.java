/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
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

package com.mmnaseri.couteau.context.contract;

import com.mmnaseri.couteau.context.error.RegistryException;

/**
 * Bean processors are interceptors that will operate on any given bean of a certain type,
 * and return a processed -- possibly different -- version of the bean in question.
 *
 * The bean processor <em>should not</em> return {@code null}, even if the case might be handled
 * by some registries.
 *
 * @see Context#addBeanProcessor(BeanProcessor)
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (6/27/13, 4:11 PM)
 */
public interface BeanProcessor<E> {

    /**
     * This processor is called exactly before the bean is put in the registry. This gives you a chance
     * to process a bean only once and update it and ensuring that the bean is pre-processed.
     * @param bean    the bean
     * @param name    the name of the bean, as registered with the context
     * @return the (processed) bean
     * @throws RegistryException
     */
    E postProcessBeforeRegistration(E bean, String name) throws RegistryException;

    /**
     * This processor will run every time the bean is being accessed through a context. This will
     * give you the chance to update information in the bean that need to be updated everytime the
     * bean is being accessed.
     * @param bean    the bean
     * @param name    the name of the bean, as registered with the context
     * @return the (processed) bean
     * @throws RegistryException
     */
    E postProcessBeforeAccess(E bean, String name) throws RegistryException;

}
