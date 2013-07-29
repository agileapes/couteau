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

package com.agileapes.couteau.graph.node.impl;

import com.agileapes.couteau.basics.api.Stringifiable;
import com.agileapes.couteau.graph.node.MutableNode;
import com.agileapes.couteau.graph.node.Node;

/**
 * The simple node is an implementation of {@link Node} that allows for
 * simple (undirected) graphs to be modeled easily. The only thing is that
 * to ensure this works, all nodes within the graph should be instances of
 * {@link SimpleNode}.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/25, 10:05)
 */
public class SimpleNode extends DirectedNode {
    
    private final Stringifiable<SimpleNode> stringifiable;

    public SimpleNode() {
        this(null);
    }

    public SimpleNode(Stringifiable<SimpleNode> stringifiable) {
        this.stringifiable = stringifiable;
    }

    @Override
    public void addNeighbor(Node neighbor) {
        if (getNeighbors().contains(neighbor)) {
            return;
        }
        super.addNeighbor(neighbor);
        if (neighbor instanceof MutableNode) {
            final MutableNode node = (MutableNode) neighbor;
            node.addNeighbor(this);
        }
    }

    @Override
    public void removeNeighbor(Node neighbor) {
        if (!getNeighbors().contains(neighbor)) {
            return;
        }
        if (neighbor instanceof MutableNode) {
            MutableNode node = (MutableNode) neighbor;
            node.removeNeighbor(this);
        }
        super.removeNeighbor(neighbor);
    }

    @Override
    public String toString() {
        if (stringifiable != null) {
            return stringifiable.toString(this);
        }
        return super.toString();
    }
}
