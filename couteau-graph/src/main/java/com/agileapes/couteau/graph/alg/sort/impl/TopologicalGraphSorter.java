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

package com.agileapes.couteau.graph.alg.sort.impl;

import com.agileapes.couteau.graph.alg.sort.GraphSorter;
import com.agileapes.couteau.graph.error.TopologicalSortFailureError;
import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.node.impl.DirectedNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class implements the topological sort algorithm. See <a href='http://en.wikipedia.org/wiki/Topological_sorting'
 * >the Wikipedia entry</a> on topological sorting for more information.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/29, 15:42)
 */
public class TopologicalGraphSorter<N extends DirectedNode<N>> implements GraphSorter<N> {

    /**
     * Attempts the topological sorting of input nodes. The nodes will be sorted in a fashion that preserves the
     * order of dependencies defined between them.
     * @param input    the collection of nodes being sorted
     * @return the sorted list of nodes
     * @throws TopologicalSortFailureError if any circular dependencies exist between the nodes, preventing them
     * from being sorted properly.
     */
    @Override
    public List<N> sort(Collection<N> input) {
        final List<N> nodes = new ArrayList<N>(input);
        final List<N> result = new ArrayList<N>();
        while (!nodes.isEmpty()) {
            final N candidate = select(nodes, result);
            if (candidate == null) {
                throw new TopologicalSortFailureError(nodes);
            }
            result.add(candidate);
            nodes.remove(candidate);
        }
        return result;
    }

    /**
     * This method will take in the nodes to be sorted, and the nodes already sorted and
     * decide on the node to be sorted next.
     * @param toSort     the remaining nodes
     * @param sorted    the nodes already sorted
     * @return a candidate node or {@link null} if no such node exists
     */
    private <N extends DirectedNode<N>> N select(Collection<N> toSort, Collection<N> sorted) {
        for (N node : toSort) {
            boolean chosen = true;
            for (Node neighbor : node.getNeighbors()) {
                //noinspection SuspiciousMethodCalls
                if (!sorted.contains(neighbor)) {
                    chosen = false;
                    break;
                }
            }
            if (chosen) {
                return node;
            }
        }
        return null;
    }

}
