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

package com.agileapes.couteau.graph.query;

import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.search.Finder;
import com.agileapes.couteau.graph.search.impl.BreadthFirstFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/26, 11:01)
 */
public class NodeQueryFinder implements Finder {

    private final Node origin;
    private final List<NodeQueryFilter> matchers;

    public NodeQueryFinder(Node origin, List<NodeQueryFilter> matchers) {
        this.origin = origin;
        this.matchers = matchers;
    }

    private List<Node> find(Node origin, NodeQueryFilter matcher) {
        return new BreadthFirstFinder(origin, matcher.forOrigin(origin)).find();
    }

    @Override
    public List<Node> find() {
        final List<Node> agenda = new ArrayList<Node>();
        if (!matchers.isEmpty()) {
            agenda.add(origin);
        }
        for (NodeQueryFilter matcher : matchers) {
            final List<Node> unexplored = new ArrayList<Node>();
            for (Node node : agenda) {
                final List<Node> found = find(node, matcher);
                for (Node foundItem : found) {
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
