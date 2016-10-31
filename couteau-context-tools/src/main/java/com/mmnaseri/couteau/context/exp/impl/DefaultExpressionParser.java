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

package com.mmnaseri.couteau.context.exp.impl;

import com.mmnaseri.couteau.context.exp.ExpressionParser;
import com.mmnaseri.couteau.reflection.beans.BeanAccessor;
import com.mmnaseri.couteau.reflection.beans.impl.MethodBeanAccessor;
import com.mmnaseri.couteau.reflection.error.NoSuchPropertyException;
import com.mmnaseri.couteau.reflection.error.PropertyAccessException;
import com.mmnaseri.couteau.strings.document.DocumentReader;
import com.mmnaseri.couteau.strings.document.impl.DefaultDocumentReader;
import com.mmnaseri.couteau.strings.replace.CallbackMatcher;
import com.mmnaseri.couteau.strings.replace.ReplaceCallback;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (14/7/6 AD, 2:56)
 */
public class DefaultExpressionParser implements ExpressionParser {

    private static interface Reader {

        Object read(String key);

    }

    private static final class MapReader implements Reader {

        private final Map<String, Object> map;

        private MapReader(Map<String, Object> map) {
            this.map = map;
        }

        @Override
        public Object read(String key) {
            if (!map.containsKey(key)) {
                throw new Error("Item <" + key + "> was not found in the current context.");
            }
            return map.get(key);
        }

    }

    private static final class ObjectReader implements Reader {

        private final BeanAccessor<Object> accessor;

        private ObjectReader(Object obj) {
            this.accessor = new MethodBeanAccessor<Object>(obj);
        }

        @Override
        public Object read(String key) {
            try {
                return accessor.getPropertyValue(key);
            } catch (NoSuchPropertyException e) {
                throw new Error("Item <" + key + "> was not found in the current context.");
            } catch (PropertyAccessException e) {
                throw new Error("Item <" + key + "> was could not be accessed in the current context.");
            }
        }
    }

    private final Reader context;

    public DefaultExpressionParser(Object context) {
        //noinspection unchecked
        this.context = context instanceof Map ? new MapReader((Map) context) : new ObjectReader(context);
    }

    @Override
    public String parse(String expression) {
        final CallbackMatcher matcher = new CallbackMatcher("\\$\\{([^\\}]+)\\}", expression);
        return matcher.replace(new ReplaceCallback() {
            @Override
            public String replace(MatchResult matchResult) {
                final String item = matchResult.group(1).trim();
                Integer index = null;
                final DocumentReader reader = new DefaultDocumentReader(item);
                final String key = reader.expect("[a-zA-Z_][a-zA-Z_0-9]+", false);
                reader.skip(Pattern.compile("\\s+"));
                if (reader.has("\\[\\s*\\d+\\s*\\]")) {
                    reader.skip(Pattern.compile("\\["));
                    index = Integer.parseInt(reader.expect("\\d+", true));
                    reader.skip(Pattern.compile("\\s+"));
                    reader.skip(Pattern.compile("\\]"));
                }
                reader.expect("$", true);
                Object value = context.read(key);
                if (index != null) {
                    if (value instanceof Collection) {
                        Collection collection = (Collection) value;
                        int i = 0;
                        final Iterator iterator = collection.iterator();
                        while (i < index) {
                            iterator.next();
                            i ++;
                        }
                        value = iterator.next();
                    } else if (value.getClass().isArray()) {
                        value = Array.get(value, index);
                    } else if (value instanceof String) {
                        value = ((String) value).charAt(index);
                    } else {
                        throw new Error("Unexpected index address: " + index);
                    }
                }
                return value == null ? "null" : value.toString();
            }
        });
    }

}
