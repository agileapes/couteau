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

package com.mmnaseri.couteau.freemarker.model;

import com.mmnaseri.couteau.basics.api.Filter;
import com.mmnaseri.couteau.freemarker.api.Invokable;

import java.util.Collection;

import static com.mmnaseri.couteau.basics.collections.CollectionWrapper.with;

/**
 * This method model is designed to help with both checking if single items match a
 * given criteria and to filter out an entire collection based on that criteria.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/8/31, 17:08)
 */
public abstract class FilteringMethodModel<E> extends TypedMethodModel {

    @Invokable
    public Collection<E> filter(Collection<E> collection) {
        //noinspection unchecked
        return with(collection).keep(new Filter<E>() {
            @Override
            public boolean accepts(E item) {
                return filter(item);
            }
        }).list();
    }

    protected abstract boolean filter(E item);

}
