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

package com.mmnaseri.couteau.xml.query.impl;

import com.mmnaseri.couteau.graph.node.NodeFilter;
import com.mmnaseri.couteau.graph.query.QuerySnippetParser;
import com.mmnaseri.couteau.strings.document.DocumentReader;
import com.mmnaseri.couteau.strings.token.impl.ContainedTokenReader;
import com.mmnaseri.couteau.xml.query.filters.NameNodeFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
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
        filters.add(new NameNodeFilter(nodeName));
        return filters;
    }

}
