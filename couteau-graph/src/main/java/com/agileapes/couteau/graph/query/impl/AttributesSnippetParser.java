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

package com.agileapes.couteau.graph.query.impl;

import com.agileapes.couteau.graph.node.NodeFilter;
import com.agileapes.couteau.graph.query.QuerySnippetParser;
import com.agileapes.couteau.graph.query.filters.AttributeNodeFilter;
import com.agileapes.couteau.strings.document.DocumentReader;
import com.agileapes.couteau.strings.document.impl.MapParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This parser generates filters from string inputs that are maps formatted as
 * '[a1=...,a2=...,...]'
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 6:16)
 */
public class AttributesSnippetParser extends QuerySnippetParser {

    @Override
    public List<NodeFilter> parse(DocumentReader reader) {
        if (!reader.hasMore() || !reader.peek(1).equals("[")) {
            return null;
        }
        final ArrayList<NodeFilter> filters = new ArrayList<NodeFilter>();
        final Map<String,String> map = reader.parse(new MapParser(MapParser.Container.SQUARE));
        for (Map.Entry<String, String> entry : map.entrySet()) {
            filters.add(new AttributeNodeFilter(entry.getKey(), entry.getValue()));
        }
        return filters;
    }

}
