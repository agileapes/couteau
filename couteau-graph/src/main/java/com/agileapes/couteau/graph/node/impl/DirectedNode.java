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

import com.agileapes.couteau.graph.node.MutableNode;
import com.agileapes.couteau.graph.node.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/24, 15:04)
 */
public class DirectedNode implements MutableNode {

    /**
     * This nodes neighbors and link weights
     */
    private final Map<Node, Double> neighbors = new HashMap<Node, Double>();

    /**
     * The map of all attribute values
     */
    private final Map<String, String> attributes = new HashMap<String, String>();

    /**
     * Will update the value of the given attribute. If this attribute has not been set
     * previously, it will be added to the node.
     * @param attributeName    the name of the attribute to be changed
     * @param value            the value of the attribute
     */
    @Override
    public void setAttribute(String attributeName, String value) {
        attributes.put(attributeName, value);
    }

    /**
     * This method adds a new neighbor to the node, with the weight being set to zero.
     * If the node is already a neighbor of this node, the weight of the connection between
     * the two will be set to zero.
     * @param neighbor    the node be added as a neighbor
     */
    @Override
    public void addNeighbor(Node neighbor) {
        if (neighbors.containsKey(neighbor)) {
            neighbors.remove(neighbor);
        }
        neighbors.put(neighbor, 0d);
    }

    @Override
    public void removeNeighbor(Node neighbor) {
        neighbors.remove(neighbor);
    }

    @Override
    public void removeAllNeighbors() {
        for (Node node : neighbors.keySet()) {
            removeNeighbor(node);
        }
    }

    /**
     * Will change the weight of the connection between the two nodes. If no link exists
     * between the nodes, this method will not change anything.
     * @param neighbor    the neighboring node
     * @param weight      the new weight of the link
     */
    @Override
    public void setLinkWeight(Node neighbor, double weight) {
        if (neighbors.containsKey(neighbor)) {
            neighbors.remove(neighbor);
            neighbors.put(neighbor, weight);
        }
    }

    /**
     * This method returns a set of the names of all the attributes associated
     * and assigned with this node
     * @return the set of attribute names
     */
    @Override
    public Set<String> getAttributeNames() {
        return attributes.keySet();
    }

    /**
     * This method returns the value assigned to the given attribute for this node
     * @param attributeName    the name of the attribute being queried for
     * @return the value of the attribute, or {@code null} if no such attribute has
     * been set for this node.
     */
    @Override
    public String getAttribute(String attributeName) {
        return attributes.containsKey(attributeName) ? attributes.get(attributeName) : null;
    }

    /**
     * This method will return a set of all the neighbors adjacent to this node. Do
     * note that this does not necessarily bind nodes in a two-way relation. This is
     * to allow for modeling of directed graphs.
     * @return the set of nodes adjacent to this node.
     */
    @Override
    public Set<Node> getNeighbors() {
        return neighbors.keySet();
    }

    /**
     * Returns the weight associated with the originating from this node to the
     * node being queried. If no link has been established between the two nodes,
     * the return value will be {@code 0}.
     * @param neighbour    the neighbor to be queried for
     * @return the weight of the link originating from this node and ending in the
     * neighboring node. If no such link exists, the value returned is {@code 0}.
     */
    @Override
    public double getLinkWeight(Node neighbour) {
        return neighbors.containsKey(neighbour) ? neighbors.get(neighbour) : 0d;
    }

}
