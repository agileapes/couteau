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

package com.mmnaseri.couteau.context.spring;

import com.mmnaseri.couteau.context.contract.Context;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * This interface indicates that the context needs to be configured to be used with Spring.
 * This way, the contexts will be easily picked up by the configurator and added to Spring's
 * context the correct way.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/3/13, 6:39 PM)
 */
public interface SpringConfigurableContext<E> extends Context<E> {

    /**
     * This method will be called to give the context a one-time-only chance to configure
     * and prepare itself for usage within Spring's context.
     * @param beanFactory    the bean factory within which the context is being configured
     */
    void configure(ConfigurableListableBeanFactory beanFactory);

}
