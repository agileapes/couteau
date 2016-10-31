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

package com.mmnaseri.couteau.graph.search.impl;

import com.mmnaseri.couteau.graph.node.Node;
import com.mmnaseri.couteau.graph.node.NodeFilter;
import com.mmnaseri.couteau.graph.search.Finder;

import java.util.*;

/**
 * <p>This finder will perform a breadth-first search on the graph, returning
 * a set of all nodes chosen by the filter.</p>
 *
 * <p><strong>See</strong> the <a href='http://en.wikipedia.org/wiki/Breadth-first_search'
 * >Wikipedia entry</a> on Breadth-first search</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/7/24, 15:29)
 */
public class BreadthFirstFinder<N extends Node<N>> implements Finder<N> {

    private final N origin;
    private final NodeFilter<N> filter;

    /**
     * @param origin     the node from which the search will originate
     * @param filter    the filter which will decide whether or not a given node
     *                   is a match
     */
    public BreadthFirstFinder(N origin, NodeFilter<N> filter) {
        this.filter = filter;
        this.origin = origin;
    }

    /**
     * This method will take a node and start searching from that node for all nodes
     * that match with the given filter. The search will be carried out in a breadth-first
     * fashion, meaning that all neighboring nodes will be searched first before
     * digging in deeper.
     * @return the set of all nodes matching the given description
     */
    @Override
    public List<N> find() {
        //this is to maintain the status of each node as seen or new.
        final Set<N> markedNodes = new HashSet<N>();
        markedNodes.add(origin);
        //this queue holds all nodes that are to be seen, and have not been examined yet
        final Queue<N> unseen = new ArrayDeque<N>();
        //we start by examining the origin node
        unseen.add(origin);
        //this list holds all nodes accepted by the filter
        final ArrayList<N> result = new ArrayList<N>();
        //we carry on until all node accessible from the originating node have been seen
        while (!unseen.isEmpty()) {
            final N node = unseen.remove();
            if (filter.accepts(node)) {
                result.add(node);
            }
            //we mark all unmarked nodes adjacent to this node as unseen
            for (Node neighbor : node.getNeighbors()) {
                //noinspection SuspiciousMethodCalls
                if (!markedNodes.contains(neighbor)) {
                    markedNodes.add(node);
                    //noinspection unchecked
                    unseen.add((N) neighbor);
                }
            }
        }
        return result;
    }

}
