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
 * <p>This is an implementation of Finder that will perform a depth-first search on the
 * origin node and its strongly connected component.</p>
 *
 * <p><strong>See</strong> the <a href='http://en.wikipedia.org/wiki/Depth-first_search'
 * >Wikipedia entry</a> on Depth-first search in a graph</p>
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/24, 16:00)
 */
public class DepthFirstFinder<N extends Node<N>> implements Finder<N> {

    private final N origin;
    private final NodeFilter<N> filter;

    /**
     * @param origin     the node from which the search will originate
     * @param filter    the filter which will decide whether or not a given node
     *                   is a match
     */
    public DepthFirstFinder(N origin, NodeFilter<N> filter) {
        this.origin = origin;
        this.filter = filter;
    }

    /**
     * This method will take a node and start searching from that node for all nodes
     * that match with the given filter. This implementation focuses on depth, meaning
     * that it will go for the first unexplored path, going as deep as possible, before
     * retracing its steps.
     * @return the set of all nodes matching the given description
     */
    @Override
    public List<N> find() {
        //this is the list of nodes accepted by the filter
        final ArrayList<N> result = new ArrayList<N>();
        //this is a stack of all nodes currently being explored
        final Stack<N> exploring = new Stack<N>();
        //this holds the set of all nodes whose depths has been examined and explored
        final Set<N> explored = new HashSet<N>();
        exploring.push(origin);
        //we continue until no more exploration is in progress
        while (!exploring.isEmpty()) {
            //we peek ahead to see what we are going to explore
            final N node = exploring.peek();
            //if this node is acceptable, we save it
            if (filter.accepts(node) && !result.contains(node)) {
                result.add(node);
            }
            //this flag determines whether the node has been fully examined
            boolean done = true;
            for (Node neighbor : node.getNeighbors()) {
                //if a node's neighbor has already been explored or is being explored
                //we do not consider it.
                //noinspection SuspiciousMethodCalls
                if (explored.contains(neighbor) || exploring.contains(neighbor)) {
                    continue;
                }
                //if even one node adjacent to the current node has not been explored
                //it breaks this node's chance at being considered fully examined.
                //noinspection unchecked
                exploring.push((N) neighbor);
                done = false;
                break;
            }
            if (done) {
                explored.add(node);
                exploring.pop();
            }
        }
        return result;
    }

}
