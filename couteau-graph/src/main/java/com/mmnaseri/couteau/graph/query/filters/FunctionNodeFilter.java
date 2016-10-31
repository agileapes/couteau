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

package com.mmnaseri.couteau.graph.query.filters;

import com.mmnaseri.couteau.graph.node.ConfigurableNodeFilter;
import com.mmnaseri.couteau.graph.node.Node;
import com.mmnaseri.couteau.graph.node.NodeFilter;

import java.util.Map;

/**
 * This will accept nodes that are subsequently accepted by given node filters.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/7/30, 5:54)
 */
public class FunctionNodeFilter<N extends Node> implements NodeFilter<N>, OriginNodeAware<N> {

    private final Map<String, String> arguments;
    private N origin;
    private final NodeFilter<N> filter;

    public FunctionNodeFilter(Map<String, NodeFilter<N>> filters, String functionName, Map<String, String> arguments) {
        this.arguments = arguments;
        for (Map.Entry<String, NodeFilter<N>> nodeFilter : filters.entrySet()) {
            if (nodeFilter.getKey().equals(functionName)) {
                filter = nodeFilter.getValue();
                return;
            }
        }
        throw new IllegalStateException("Invalid function name: " + functionName);
    }

    @Override
    public void setOrigin(N origin) {
        this.origin = origin;
    }

    @Override
    public boolean accepts(N item) {
        if (filter instanceof OriginNodeAware<?>) {
            //noinspection unchecked
            ((OriginNodeAware<N>) filter).setOrigin(origin);
        }
        int index = 0;
        if (filter instanceof ConfigurableNodeFilter) {
            final ConfigurableNodeFilter nodeFilter = (ConfigurableNodeFilter) filter;
            for (Map.Entry<String, String> argument : arguments.entrySet()) {
                String key = argument.getKey().trim();
                String value = argument.getValue();
                if (value == null) {
                    value = key;
                    key = Integer.toString(index);
                }
                nodeFilter.setAttribute(key, value);
                index ++;
            }
        }
        return filter.accepts(item);
    }

}
