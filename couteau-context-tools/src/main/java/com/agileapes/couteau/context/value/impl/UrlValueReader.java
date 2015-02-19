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

package com.agileapes.couteau.context.value.impl;

import com.agileapes.couteau.context.error.InvalidInputValueError;
import com.agileapes.couteau.context.error.InvalidValueTypeError;
import com.agileapes.couteau.context.value.ValueReader;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This value reader will take URLs and URIs and read them from their textual representations.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/5, 15:42)
 */
public class UrlValueReader implements ValueReader {

    /**
     * @param type    the type to be queried
     * @return {@code true} if it is either a {@link URL} or a {@link URI}
     */
    @Override
    public boolean canRead(Class<?> type) {
        return URL.class.equals(type) || URI.class.equals(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E read(String text, Class<E> type) {
        if (!canRead(type)) {
            throw new InvalidValueTypeError(type);
        }
        if (type.equals(URL.class)) {
            try {
                return (E) new URL(text);
            } catch (MalformedURLException e) {
                throw new InvalidInputValueError(text, type, e);
            }
        } else {
            try {
                return (E) new URI(text);
            } catch (URISyntaxException e) {
                throw new InvalidInputValueError(text, type, e);
            }
        }
    }

}
