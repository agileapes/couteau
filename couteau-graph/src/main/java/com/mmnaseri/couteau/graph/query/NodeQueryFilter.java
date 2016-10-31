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

package com.mmnaseri.couteau.graph.query;

import com.mmnaseri.couteau.basics.api.Filter;
import com.mmnaseri.couteau.basics.api.impl.FilterChain;
import com.mmnaseri.couteau.graph.node.Node;
import com.mmnaseri.couteau.graph.node.NodeFilter;
import com.mmnaseri.couteau.graph.query.filters.OriginNodeAware;

/**
 * NodeQueryFilter is a filter chain that will hold all other filters applicable throughout the search
 * process.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/7/26, 11:03)
 */
public class NodeQueryFilter<N extends Node> extends FilterChain<N> implements NodeFilter<N> {

    /**
     * will change the origin of the search
     *
     * @param origin    the new origin
     * @return will return the current filter (for chaining purposes)
     */
    public NodeQueryFilter<?> forOrigin(Node origin) {
        for (Filter<? super N> filter : filters) {
            if (filter instanceof OriginNodeAware) {
                //noinspection unchecked
                ((OriginNodeAware) filter).setOrigin(origin);
            }
        }
        return this;
    }

}
