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

package com.agileapes.couteau.graph.node;

/**
 * This interface will allow for mutation of attributes assigned to the node.
 * By implementing this interface, this node will allow external containers
 * to modify it.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/24, 14:51)
 */
public interface MutableNode extends Node {

    /**
     * Will update the value of the given attribute. If this attribute has not been set
     * previously, it will be added to the node.
     * @param attributeName    the name of the attribute to be changed
     * @param value            the value of the attribute
     */
    void setAttribute(String attributeName, String value);

    /**
     * This method adds a new neighbor to the node, with the weight being set to zero.
     * If the node is already a neighbor of this node, the weight of the connection between
     * the two will be set to zero.
     * @param neighbor    the node be added as a neighbor
     */
    void addNeighbor(Node neighbor);

    /**
     * This method will remove a neighboring node from this node, essentially cutting this
     * side of the relationship
     * @param neighbor    the neighbor to be removed
     */
    void removeNeighbor(Node neighbor);

    /**
     * This method removes all neighbors from this node, making it a dead-end, if not disconnecting it
     * completely.
     */
    void removeAllNeighbors();

    /**
     * Will change the weight of the connection between the two nodes. If no link exists
     * between the nodes, this method will not change anything.
     * @param neighbor    the neighboring node
     * @param weight      the new weight of the link
     */
    void setLinkWeight(Node neighbor, double weight);

}
