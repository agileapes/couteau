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

package com.agileapes.couteau.context.data.impl;

import com.agileapes.couteau.context.data.DataProperty;
import com.agileapes.couteau.context.value.ValueReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/21 AD, 17:19)
 */
public class PropertyMapIterator<P> implements Iterator<DataProperty<P>> {

    private final Map<P, String> values;
    private final ValueReader valueReader;

    private List<P> keys;
    private int index;

    public PropertyMapIterator(Map<P, String> values, ValueReader valueReader) {
        this.values = values;
        this.valueReader = valueReader;
        keys = new ArrayList<P>(values.keySet());
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < values.size();
    }

    @Override
    public DataProperty<P> next() {
        final ImmutableDataProperty<P> property = new ImmutableDataProperty<P>(keys.get(index), values.get(keys.get(index)), valueReader);
        index ++;
        return property;
    }

    @Override
    public void remove() {
        values.remove(keys.get(index));
        keys.remove(index);
    }

}
