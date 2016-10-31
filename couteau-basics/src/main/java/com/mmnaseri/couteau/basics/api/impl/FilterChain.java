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

package com.mmnaseri.couteau.basics.api.impl;

import com.mmnaseri.couteau.basics.api.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class acts as an object-oriented representation of a logical AND between many
 * given filters; i.e., should any one of these filters fail, the whole filter will fail.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/7/30, 5:34)
 */
public class FilterChain<E> implements Filter<E> {

    protected final List<Filter<? super E>> filters = new ArrayList<Filter<? super E>>();

    @Override
    public boolean accepts(E item) {
        for (Filter<? super E> filter : filters) {
            if (!filter.accepts(item)) {
                return false;
            }
        }
        return true;
    }

    public void addFilter(Filter<? super E> filter) {
        filters.add(filter);
    }

}
