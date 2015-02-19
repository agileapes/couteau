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

package com.agileapes.couteau.http.util;

import com.agileapes.couteau.http.io.HttpHeader;
import com.agileapes.couteau.http.io.impl.ImmutableHttpHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/16 AD, 16:07)
 */
public final class HttpHeaderBuilder {

    private HttpHeaderBuilder() {}

    public static List<HttpHeader> buildFromMap(Map<String, String> map) {
        final ArrayList<HttpHeader> headers = new ArrayList<HttpHeader>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            headers.add(new ImmutableHttpHeader(entry.getKey(), entry.getValue()));
        }
        return headers;
    }

    public static List<HttpHeader> buildFromArray(String[][] headers) {
        final HashMap<String, String> map = new HashMap<String, String>();
        for (String[] header : headers) {
            if (header.length == 0) {
                continue;
            }
            map.put(header[0], header.length > 1 ? header[1] : "");
        }
        return buildFromMap(map);
    }

}
