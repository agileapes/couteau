/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.couteau.graph.query.impl;

import com.agileapes.couteau.graph.node.NodeFilter;
import com.agileapes.couteau.graph.query.QuerySnippetParser;
import com.agileapes.couteau.graph.query.filters.FunctionNodeFilter;
import com.agileapes.couteau.strings.document.DocumentReader;
import com.agileapes.couteau.strings.document.impl.MapParser;
import com.agileapes.couteau.strings.token.impl.IdentifierTokenReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This parser will parse snippets denoting a chain of function calls
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 6:21)
 */
public class FunctionCallSnippetParser extends QuerySnippetParser {

    private final Map<String, NodeFilter> filters;

    public FunctionCallSnippetParser(Map<String, NodeFilter> filters) {
        this.filters = filters;
    }

    @Override
    public List<NodeFilter> parse(DocumentReader reader) {
        if (!reader.hasMore() || !reader.peek(1).equals("{")) {
            return null;
        }
        final ArrayList<NodeFilter> filters = new ArrayList<NodeFilter>();
        reader.expect("\\{", false);
        while (true) {
            if (reader.peek(1).equals("}")) {
                reader.nextChar();
                break;
            }
            final String name = reader.read(reader.expectToken(new IdentifierTokenReader()));
            final Map<String,String> arguments = reader.parse(new MapParser(MapParser.Container.ROUNDED));
            reader.expect(";", true);
            //noinspection unchecked
            final FunctionNodeFilter<?> filter = new FunctionNodeFilter(this.filters, name, arguments);
            filters.add(filter);
        }
        return filters;
    }

}
