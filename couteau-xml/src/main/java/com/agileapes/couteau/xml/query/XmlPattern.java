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

package com.agileapes.couteau.xml.query;

import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.query.NodeFilterRepository;
import com.agileapes.couteau.graph.query.NodePattern;
import com.agileapes.couteau.graph.query.NodeQueryFilter;
import com.agileapes.couteau.graph.query.NodeQueryFinder;
import com.agileapes.couteau.graph.query.impl.*;
import com.agileapes.couteau.graph.search.Finder;
import com.agileapes.couteau.xml.query.filters.NodeTypeFilter;
import com.agileapes.couteau.xml.query.filters.NodeValueFilter;
import com.agileapes.couteau.xml.query.impl.NodeNameSnippetParser;

import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 13:41)
 */
public class XmlPattern implements NodePattern {

    static {
        NodeFilterRepository.addFilter("type", new NodeTypeFilter());
        NodeFilterRepository.addFilter("value", new NodeValueFilter());
    }

    public static NodePattern compile(String pattern) {
        final DefaultPatternCompiler compiler = new DefaultPatternCompiler();
        compiler.addParser(new ImmediateSnippetParser());
        compiler.addParser(new WildcardSnippetParser());
        compiler.addParser(new NodeNameSnippetParser());
        compiler.addParser(new NodeIndexSnippetParser());
        compiler.addParser(new AttributesSnippetParser());
        compiler.addParser(new FunctionCallSnippetParser(NodeFilterRepository.getFilters()));
        return new XmlPattern(compiler.compile(pattern));
    }

    private final List<NodeQueryFilter<?>> filters;

    public XmlPattern(List<NodeQueryFilter<?>> filters) {
        this.filters = filters;
    }

    @Override
    public <N extends Node> Finder<N> finder(N origin) {
        return new NodeQueryFinder<N>(origin, filters);
    }

}
