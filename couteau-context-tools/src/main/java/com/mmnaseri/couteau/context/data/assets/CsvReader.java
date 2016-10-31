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

import com.mmnaseri.couteau.context.data.DataSheet;
import com.mmnaseri.couteau.context.data.impl.DefaultDataRow;
import com.mmnaseri.couteau.context.data.impl.DefaultDataSheet;
import com.mmnaseri.couteau.context.data.impl.ImmutableDataRow;
import com.mmnaseri.couteau.context.value.ValueReader;
import com.mmnaseri.couteau.strings.document.DocumentReader;
import com.mmnaseri.couteau.strings.document.impl.DefaultDocumentReader;
import com.mmnaseri.couteau.strings.document.impl.ListParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/21 AD, 17:21)
 */
public class CsvReader {

    private final ValueReader valueReader;

    public CsvReader(ValueReader valueReader) {
        this.valueReader = valueReader;
    }

    public DataSheet<String> read(String document) {
        final ListParser listParser = new ListParser(null, "\n|$", "\"'`");
        final DefaultDataSheet<String> dataSheet = new DefaultDataSheet<String>();
        final DocumentReader reader = new DefaultDocumentReader(document);
        final List<String> properties = reader.parse(listParser);
        for (String property : properties) {
            dataSheet.addProperty(property);
        }
        while (reader.hasMore()) {
            final List<String> values = reader.parse(listParser);
            final DefaultDataRow<String> row = new DefaultDataRow<String>(valueReader, dataSheet);
            for (int i = 0; i < values.size(); i++) {
                row.set(properties.get(i), values.get(i));
            }
            dataSheet.addRow(new ImmutableDataRow<String>(valueReader, row));
        }
        return dataSheet;
    }
    
    public DataSheet<String> read(InputStream inputStream) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder builder = new StringBuilder();
        for (String line; (line = reader.readLine()) != null;) {
            builder.append(line).append("\n");
        }
        reader.close();
        return read(builder.toString());
    }

}
