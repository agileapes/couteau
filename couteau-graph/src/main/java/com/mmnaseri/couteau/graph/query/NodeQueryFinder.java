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

import com.mmnaseri.couteau.graph.node.Node;
import com.mmnaseri.couteau.graph.node.NodeFilter;
import com.mmnaseri.couteau.graph.search.Finder;
import com.mmnaseri.couteau.graph.search.impl.BreadthFirstFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides an implementation of Finder that relies on {@link BreadthFirstFinder} for
 * looking up nodes that are accepted by a given filter chain of {@link NodeFilter}s
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/7/26, 11:01)
 */
public class NodeQueryFinder<N extends Node<N>> implements Finder<N> {

    private final N origin;
    private final List<NodeQueryFilter<?>> filters;

    public NodeQueryFinder(N origin, List<NodeQueryFilter<?>> filters) {
        this.origin = origin;
        this.filters = filters;
    }

    private List<N> find(N origin, NodeQueryFilter<?> matcher) {
        //noinspection unchecked
        return new BreadthFirstFinder<N>(origin, (NodeFilter<N>) matcher.forOrigin(origin)).find();
    }

    @Override
    public List<N> find() {
        final List<N> agenda = new ArrayList<N>();
        if (!filters.isEmpty()) {
            agenda.add(origin);
        }
        for (NodeQueryFilter<?> matcher : filters) {
            final List<N> unexplored = new ArrayList<N>();
            for (N node : agenda) {
                final List<N> found = find(node, matcher);
                for (N foundItem : found) {
                    if (!unexplored.contains(foundItem)) {
                        unexplored.add(foundItem);
                    }
                }
            }
            agenda.clear();
            agenda.addAll(unexplored);
        }
        return agenda;
    }

}
