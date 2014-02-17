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

package com.agileapes.couteau.xml.query.impl;

import com.agileapes.couteau.graph.node.NodeFilter;
import com.agileapes.couteau.graph.query.QuerySnippetParser;
import com.agileapes.couteau.strings.document.DocumentReader;
import com.agileapes.couteau.strings.token.impl.ContainedTokenReader;
import com.agileapes.couteau.xml.query.filters.NodeNameFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 13:45)
 */
public class NodeNameSnippetParser extends QuerySnippetParser {

    @Override
    public List<NodeFilter> parse(DocumentReader reader) {
        if (!reader.hasMore() || reader.has("([\\[\\{]|#\\d)")) {
            return null;
        }
        final String nodeName = reader.read(reader.expectToken(new ContainedTokenReader("'\"`", Pattern.compile("([\\[\\{\\s/]|#\\d|$)")))).trim();
        final ArrayList<NodeFilter> filters = new ArrayList<NodeFilter>();
        filters.add(new NodeNameFilter(nodeName));
        return filters;
    }

}
