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

/**
 * Ordered beans allow you to prioritize over beans. The lowest order means highest priority. Thus, by assigning
 * an order of {@link #HIGHEST_PRECEDENCE} you will get the lowest order and highest priority.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (6/27/13, 4:09 PM)
 */
public interface OrderedBean {

    /**
     * The highest precedence. Makes the bean have higher priority than any other bean.
     */
    public static final int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * Gives the bean the least importance
     */
    public static final int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * Makes the bean of no special priority
     */
    public static final int NEUTRAL_PRECEDENCE = 0;

    /**
     * @return the order of the bean as deemed by the bean itself
     */
    int getOrder();

}
