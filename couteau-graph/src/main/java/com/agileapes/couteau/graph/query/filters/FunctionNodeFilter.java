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

package com.agileapes.couteau.graph.query.filters;

import com.agileapes.couteau.graph.node.ConfigurableNamedNodeFilter;
import com.agileapes.couteau.graph.node.NamedNodeFilter;
import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.node.NodeFilter;

import java.util.Map;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 5:54)
 */
public class FunctionNodeFilter implements NodeFilter, OriginNodeAware {

    private final Map<String, String> arguments;
    private Node origin;
    private final NamedNodeFilter filter;

    public FunctionNodeFilter(Set<NamedNodeFilter> filters, String functionName, Map<String, String> arguments) {
        this.arguments = arguments;
        for (NamedNodeFilter nodeFilter : filters) {
            if (nodeFilter.getName().equals(functionName)) {
                filter = nodeFilter;
                return;
            }
        }
        throw new IllegalStateException("Invalid function name: " + functionName);
    }

    @Override
    public void setOrigin(Node origin) {
        this.origin = origin;
    }

    @Override
    public boolean accepts(Node item) {
        if (filter instanceof OriginNodeAware) {
            ((OriginNodeAware) filter).setOrigin(origin);
        }
        if (filter instanceof ConfigurableNamedNodeFilter) {
            ConfigurableNamedNodeFilter nodeFilter = (ConfigurableNamedNodeFilter) filter;
            for (Map.Entry<String, String> entry : arguments.entrySet()) {
                nodeFilter.setAttribute(entry.getKey(), entry.getValue());
            }
        }
        return filter.accepts(item);
    }
}
