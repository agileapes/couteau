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

package com.mmnaseri.couteau.reflection.convert;

import com.mmnaseri.couteau.reflection.property.PropertyDescriptor;

/**
 * A conversion strategy is a central way for abstracting the process of deciding which items should be
 * converted and which items do not need that.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/13/13, 7:31 AM)
 */
public interface ConversionStrategy {

    /**
     * Will make a decision based on the input property descriptor.
     * @param propertyDescriptor    the property descriptor for which a conversion decision is needed
     * @return the decision that determines which action should be taken
     */
    ConversionDecision decide(PropertyDescriptor propertyDescriptor);

}
