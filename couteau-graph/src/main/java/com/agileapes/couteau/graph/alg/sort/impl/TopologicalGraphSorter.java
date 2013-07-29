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
import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.node.NodeFilter;
import com.agileapes.couteau.graph.node.impl.DirectedNode;
import com.agileapes.couteau.graph.search.impl.BreadthFirstFinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/29, 15:42)
 */
public class TopologicalGraphSorter implements GraphSorter<DirectedNode> {

    @Override
    public <N extends DirectedNode> List<N> sort(N origin) {
        final List<Node> nodes = new BreadthFirstFinder(origin, new NodeFilter() {
            @Override
            public boolean accepts(Node item) {
                return true;
            }
        }).find();

        final ArrayList<N> result = new ArrayList<N>();
        while (!nodes.isEmpty()) {
            final Set<Node> candidates = select(nodes);
            if (candidates.isEmpty()) {
                return null;
            }
            final Node chosen = candidates.iterator().next();
            //noinspection unchecked
            result.add((N) chosen);
            nodes.remove(chosen);
        }
        return result;
    }

    private Set<Node> select(List<Node> nodes) {
        final HashSet<Node> candidates = new HashSet<Node>();
        for (Node node : nodes) {
            if (incoming(nodes, node).isEmpty()) {
                candidates.add(node);
            }
        }
        return candidates;
    }

    private Set<Node> incoming(List<Node> nodes, Node node) {
        final HashSet<Node> incomingNodes = new HashSet<Node>();
        for (Node neighbor : nodes) {
            if (neighbor.getNeighbors().contains(node)) {
                incomingNodes.add(neighbor);
            }
        }
        return incomingNodes;
    }

}
