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

package com.agileapes.couteau.graph.tree.node.impl;

import com.agileapes.couteau.basics.api.Stringifiable;
import com.agileapes.couteau.graph.node.MutableNode;
import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.tree.node.TreeNode;

/**
 * This is an extension to the {@link DirectedTreeNode} adding the property that
 * all edges become bidirectional.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/25, 10:14)
 */
public class SimpleTreeNode extends DirectedTreeNode implements TreeNode {
    
    private final Stringifiable<SimpleTreeNode> stringifiable;

    public SimpleTreeNode() {
        this(null);
    }

    public SimpleTreeNode(Stringifiable<SimpleTreeNode> stringifiable) {
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
    public String toString() {
        if (stringifiable != null) {
            return stringifiable.toString(this);
        }
        return super.toString();
    }

}
