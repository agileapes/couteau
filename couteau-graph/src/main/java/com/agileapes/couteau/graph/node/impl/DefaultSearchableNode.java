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

import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.node.SearchableNode;
import com.agileapes.couteau.graph.query.GraphNodePattern;

import java.util.List;
import java.util.Set;

/**
 * This is essentially a wrapper class that allows for normal nodes to be
 * made searchable through {@link #find(String)}. All other methods are
 * delegates to the original node's methods.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/27, 12:50)
 */
public class DefaultSearchableNode implements SearchableNode<DefaultSearchableNode> {

    private final Node node;

    public DefaultSearchableNode(Node node) {
        this.node = node;
    }

    @Override
    public List<DefaultSearchableNode> find(String pattern) {
        return GraphNodePattern.compile(pattern).finder((this)).find();
    }

    @Override
    public Set<String> getAttributeNames() {
        return node.getAttributeNames();
    }

    @Override
    public String getAttribute(String attributeName) {
        return node.getAttribute(attributeName);
    }

    @Override
    public Set<Node> getNeighbors() {
        return node.getNeighbors();
    }

    @Override
    public double getLinkWeight(Node neighbour) {
        return node.getLinkWeight(neighbour);
    }

    @Override
    public Object getUserData(String key) {
        return node.getUserData(key);
    }

    @Override
    public String toString() {
        return "searchable[" + node + "]";
    }
}
