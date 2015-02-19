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

package com.agileapes.couteau.context.contract;

import com.agileapes.couteau.context.error.RegistryException;

import java.util.Date;
import java.util.Map;

/**
 * The context is basically an extension to the capabilities offered by a simple
 * {@link Registry}, allowing beans to be more in-context with each other, and offering
 * possibilities of event-driven hooking.
 *
 * It also extends {@link EventPublisher} to allow multi-casting events inside the context.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:09 PM)
 */
public interface Context<E> extends Registry<E>, EventPublisher {

    /**
     * This method will add context processors to the context. The context processors will run
     * once the context has been readied for use.
     *
     * You might need to refresh the context if the context is reconfigurable
     * @see ReconfigurableContext
     * @param processor    the processor requesting access to the context
     * @return the context itself. This is returned for chaining.
     */
    Context<E> addContextProcessor(ContextProcessor<E> processor);

    /**
     * This method will add bean processors to the context. Bean processors, since the moment they
     * are added to the system, will be able to manipulate the process with which the beans
     * ae registered with the underlying registry.
     * @param processor    the processor
     * @return the context itself. This is returned for chaining purposes.
     */
    Context<E> addBeanProcessor(BeanProcessor<E> processor);

    /**
     * @return the time in milliseconds in which the context was set up and readied for use.
     */
    long getStartupTime();

    /**
     * @return the date object representing the moment at which the context was marked as ready
     * for use
     */
    Date getStartupDate();

    /**
     * Will register a bean with the context, determining the name automatically
     * @param item    the item to be registered
     * @return the context itself
     * @throws RegistryException
     */
    Context<E> register(E item) throws RegistryException;

    /**
     * Will return a transform of beans with their names as keys that are the subtype of the given
     * type
     * @param type    the desired type for which the lookup will be performed
     * @param <T>     the type argument
     * @return a transform of bean names to bean instances for the given type
     * @throws RegistryException
     */
    <T extends E> Map<String, T> getBeansOfType(Class<T> type) throws RegistryException;

}
