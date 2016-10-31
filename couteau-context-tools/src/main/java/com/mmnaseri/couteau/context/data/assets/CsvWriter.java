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

package com.mmnaseri.couteau.context.data.assets;

import com.mmnaseri.couteau.context.data.DataRow;
import com.mmnaseri.couteau.context.data.DataSheet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/22 AD, 11:46)
 */
public class CsvWriter {

    public <P> void write(DataSheet<P> dataSheet, Writer writer) throws IOException {
        final List<P> properties = new ArrayList<P>(dataSheet.getProperties());
        Collections.sort(properties, new Comparator<P>() {
            @Override
            public int compare(P first, P second) {
                if (first instanceof Comparable && second instanceof Comparable) {
                    //noinspection unchecked
                    return ((Comparable) first).compareTo(second);
                }
                return 0;
            }
        });
        for (int i = 0; i < properties.size(); i++) {
            P property = properties.get(i);
            if (i > 0) {
                writer.write(",");
            }
            final String propertyName = escape(property.toString());
            writer.write("\"" + propertyName + "\"");
        }
        writer.write("\n");
        for (DataRow<P> row : dataSheet) {
            for (int i = 0; i < properties.size(); i++) {
                if (i > 0) {
                    writer.write(",");
                }
                P property = properties.get(i);
                final String value = row.getValue(property);
                writer.write("\"" + escape(value) + "\"");
            }
            writer.write("\n");
        }
    }

    public <P> void write(DataSheet<P> dataSheet, OutputStream outputStream) throws IOException {
        write(dataSheet, new OutputStreamWriter(outputStream));
    }

    private String escape(String string) {
        return string.replaceAll("\"", "\\\"");
    }

}
