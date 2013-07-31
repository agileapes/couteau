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

package com.agileapes.couteau.graph.query;

import com.agileapes.couteau.graph.node.NodeFilter;
import com.agileapes.couteau.strings.document.SnippetParser;

import java.util.List;

/**
 * This class allows for specifying snippet parsers used by {@link com.agileapes.couteau.graph.query.impl.DefaultPatternCompiler}
 * through which the pattern being compiled can be customized.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 6:02)
 */
public abstract class QuerySnippetParser implements SnippetParser<List<NodeFilter>> {

    /**
     * This method determines whether having the parser match the current input should end the
     * current section of the input pattern or not.
     * @return {@code true} means that this parser is basically a section delimiter
     */
    public boolean endsParsing() {
        return false;
    }

}
