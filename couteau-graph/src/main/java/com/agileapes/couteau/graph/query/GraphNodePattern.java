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

import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.query.impl.*;
import com.agileapes.couteau.graph.search.Finder;

import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/25, 14:47)
 */
public class GraphNodePattern implements NodePattern {

    public static NodePattern compile(String pattern) {
        final DefaultPatternCompiler compiler = new DefaultPatternCompiler();
        compiler.addParser(new ImmediateSnippetParser());
        compiler.addParser(new WildcardSnippetParser());
        compiler.addParser(new AttributesSnippetParser());
        compiler.addParser(new FunctionCallSnippetParser(NodeFilterRepository.getFilters()));
        return new GraphNodePattern(compiler.compile(pattern));
    }

    private final List<NodeQueryFilter<?>> filters;

    private GraphNodePattern(List<NodeQueryFilter<?>> filters) {
        this.filters = filters;
    }

    @Override
    public <N extends Node<N>> Finder<N> finder(N origin) {
        return new NodeQueryFinder<N>(origin, filters);
    }

}
