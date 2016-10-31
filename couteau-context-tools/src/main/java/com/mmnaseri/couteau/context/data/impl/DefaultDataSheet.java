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

import com.mmnaseri.couteau.context.data.DataRow;
import com.mmnaseri.couteau.context.data.DataSheet;

import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/21 AD, 17:15)
 */
public class DefaultDataSheet<P> implements DataSheet<P> {

    private final Set<P> properties = new HashSet<P>();
    private final List<DataRow<P>> rows = new ArrayList<DataRow<P>>();

    public void addProperty(P property) {
        properties.add(property);
    }

    public void addRow(DataRow<P> row) {
        rows.add(row);
    }

    @Override
    public Set<P> getProperties() {
        return properties;
    }

    @Override
    public int size() {
        return rows.size();
    }

    @Override
    public DataRow<P> get(int index) throws IndexOutOfBoundsException {
        return rows.get(index);
    }

    @Override
    public Iterator<DataRow<P>> iterator() {
        return rows.iterator();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (DataRow<P> row : rows) {
            builder.append(row).append("\n");
        }
        return builder.toString();
    }
}
