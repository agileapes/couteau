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

package com.mmnaseri.couteau.xml.query;

import com.mmnaseri.couteau.graph.node.Node;
import com.mmnaseri.couteau.graph.query.NodeFilterRepository;
import com.mmnaseri.couteau.graph.query.NodePattern;
import com.mmnaseri.couteau.graph.query.NodeQueryFilter;
import com.mmnaseri.couteau.graph.query.NodeQueryFinder;
import com.mmnaseri.couteau.graph.query.impl.*;
import com.mmnaseri.couteau.graph.search.Finder;
import com.mmnaseri.couteau.xml.query.filters.NamespaceNodeFilter;
import com.mmnaseri.couteau.xml.query.filters.TypeNodeFilter;
import com.mmnaseri.couteau.xml.query.filters.ValueNodeFilter;
import com.mmnaseri.couteau.xml.query.impl.NodeNameSnippetParser;

import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 13:41)
 */
public class XmlPattern implements NodePattern {

    static {
        NodeFilterRepository.addFilter("type", new TypeNodeFilter());
        NodeFilterRepository.addFilter("value", new ValueNodeFilter());
        NodeFilterRepository.addFilter("namespace", new NamespaceNodeFilter());
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
    public <N extends Node<N>> Finder<N> finder(N origin) {
        return new NodeQueryFinder<N>(origin, filters);
    }

}
