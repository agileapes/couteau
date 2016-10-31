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

package com.mmnaseri.couteau.context.data.impl;

import com.mmnaseri.couteau.context.data.DataProperty;
import com.mmnaseri.couteau.context.data.DataRow;
import com.mmnaseri.couteau.context.data.DataSheet;
import com.mmnaseri.couteau.context.value.ValueReader;
import com.mmnaseri.couteau.context.value.ValueReaderContext;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/21 AD, 17:11)
 */
public class ImmutableDataRow<P> implements DataRow<P> {

    private final Map<P, String> values = new HashMap<P, String>();
    private final ValueReader valueReader;
    private final DataSheet<P> dataSheet;

    public ImmutableDataRow(ValueReader valueReader, DataRow<P> dataRow) {
        this(valueReader, dataRow.getDataSheet());
        for (DataProperty<P> property : dataRow) {
            values.put(property.getKey(), property.getValue());
        }
    }

    public ImmutableDataRow(ValueReader valueReader, DataSheet<P> dataSheet) {
        this.valueReader = valueReader;
        this.dataSheet = dataSheet;
    }

    @Override
    public String getValue(P property) {
        return values.get(property);
    }

    @Override
    public <E> E getValue(P property, Class<E> targetType) {
        final String value = getValue(property);
        if (value == null) {
            return null;
        }
        return valueReader.read(value, targetType);
    }

    @Override
    public Object getValue(P property, Type targetType) {
        final String value = getValue(property);
        if (value == null) {
            return null;
        }
        if (valueReader instanceof ValueReaderContext) {
            ValueReaderContext context = (ValueReaderContext) valueReader;
            return context.read(value, targetType);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public DataSheet<P> getDataSheet() {
        return dataSheet;
    }

    @Override
    public Iterator<DataProperty<P>> iterator() {
        return new PropertyMapIterator<P>(values, valueReader);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<P, String> entry : values.entrySet()) {
            if (!first) {
                builder.append(",");
            }
            first = false;
            builder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return builder.toString();
    }
}
