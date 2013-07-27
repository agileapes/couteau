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

package com.agileapes.couteau.graph.search.impl;

import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.node.NodeFilter;
import com.agileapes.couteau.graph.search.Finder;

import java.util.*;

/**
 * <p>This finder will perform a breadth-first search on the graph, returning
 * a set of all nodes chosen by the filter.</p>
 *
 * <p><strong>See</strong> the <a href='http://en.wikipedia.org/wiki/Breadth-first_search'
 * >Wikipedia entry</a> on Breadth-first search</p>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/24, 15:29)
 */
public class BreadthFirstFinder implements Finder {

    private final Node origin;
    private final NodeFilter filter;

    /**
     * @param origin     the node from which the search will originate
     * @param filter    the filter which will decide whether or not a given node
     *                   is a match
     */
    public BreadthFirstFinder(Node origin, NodeFilter filter) {
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
    public List<Node> find() {
        //this is to maintain the status of each node as seen or new.
        final Set<Node> markedNodes = new HashSet<Node>();
        markedNodes.add(origin);
        //this queue holds all nodes that are to be seen, and have not been examined yet
        final Queue<Node> unseen = new ArrayDeque<Node>();
        //we start by examining the origin node
        unseen.add(origin);
        //this list holds all nodes accepted by the filter
        final ArrayList<Node> result = new ArrayList<Node>();
        //we carry on until all node accessible from the originating node have been seen
        while (!unseen.isEmpty()) {
            final Node node = unseen.remove();
            if (filter.accepts(node)) {
                result.add(node);
            }
            //we mark all unmarked nodes adjacent to this node as unseen
            for (Node neighbor : node.getNeighbors()) {
                if (!markedNodes.contains(neighbor)) {
                    markedNodes.add(node);
                    unseen.add(neighbor);
                }
            }
        }
        return result;
    }

}
