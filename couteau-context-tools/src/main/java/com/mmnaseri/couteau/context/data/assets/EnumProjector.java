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

import com.mmnaseri.couteau.basics.api.Transformer;
import com.mmnaseri.couteau.context.data.DataProperty;
import com.mmnaseri.couteau.context.data.DataRow;
import com.mmnaseri.couteau.context.data.DataSheet;
import com.mmnaseri.couteau.context.data.impl.DefaultDataRow;
import com.mmnaseri.couteau.context.data.impl.DefaultDataSheet;
import com.mmnaseri.couteau.context.data.impl.ImmutableDataRow;
import com.mmnaseri.couteau.context.value.ValueReader;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (14/6/21 AD, 18:06)
 */
public class EnumProjector<E extends Enum> {

    private final Class<E> enumType;
    private final ValueReader valueReader;

    public EnumProjector(Class<E> enumType, ValueReader valueReader) {
        this.enumType = enumType;
        this.valueReader = valueReader;
    }

    public DataSheet<E> project(DataSheet<String> dataSheet) {
        return project(dataSheet, new Transformer<String, String>() {
            @Override
            public String map(String input) {
                return input;
            }
        });
    }

    public DataSheet<E> project(DataSheet<String> dataSheet, Transformer<String, String> keyTransformer) {
        final DefaultDataSheet<E> result = new DefaultDataSheet<E>();
        for (E e : enumType.getEnumConstants()) {
            result.addProperty(e);
        }
        for (DataRow<String> row : dataSheet) {
            final DefaultDataRow<E> dataRow = new DefaultDataRow<E>(valueReader, result);
            for (DataProperty<String> property : row) {
                dataRow.set(enumType.cast(Enum.valueOf(enumType, keyTransformer.map(property.getKey()))), property.getValue());
            }
            result.addRow(new ImmutableDataRow<E>(valueReader, dataRow));
        }
        return result;
    }

}
