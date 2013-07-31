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

package com.agileapes.couteau.graph.error;

import com.agileapes.couteau.graph.node.Node;

import java.util.Collection;

/**
 * <p>This error indicates a problem with the nodes being sorted; namely that of having a circular dependency
 * among the nodes of a directed graph, a problem which would prevent the nodes from presenting candidates
 * for the sorting process.</p>
 *
 * <p>The problematic nodes will be contained in {@link #getNodes()}.</p>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/31/13, 10:32 AM)
 */
public class TopologicalSortFailureError extends Error {

    private final Collection<? extends Node> nodes;

    public TopologicalSortFailureError(Collection<? extends Node> nodes) {
        super("Failed to sort nodes. There is a circular dependency between: " + nodes);
        this.nodes = nodes;
    }

    public Collection<? extends Node> getNodes() {
        return nodes;
    }

}
