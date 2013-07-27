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

import java.util.Set;

/**
 * This is the abstraction of a node. The graphs in this framework are modeled through
 * the nodes.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/24, 14:42)
 */
public interface Node {

    /**
     * This method returns a set of the names of all the attributes associated
     * and assigned with this node
     * @return the set of attribute names
     */
    Set<String> getAttributeNames();

    /**
     * This method returns the value assigned to the given attribute for this node
     * @param attributeName    the name of the attribute being queried for
     * @return the value of the attribute, or {@code null} if no such attribute has
     * been set for this node.
     */
    String getAttribute(String attributeName);

    /**
     * This method will return a set of all the neighbors adjacent to this node. Do
     * note that this does not necessarily bind nodes in a two-way relation. This is
     * to allow for modeling of directed graphs.
     * @return the set of nodes adjacent to this node.
     */
    Set<Node> getNeighbors();

    /**
     * Returns the weight associated with the originating from this node to the
     * node being queried. If no link has been established between the two nodes,
     * the return value will be {@code 0}.
     * @param neighbour    the neighbor to be queried for
     * @return the weight of the link originating from this node and ending in the
     * neighboring node. If no such link exists, the value returned is {@code 0}.
     */
    double getLinkWeight(Node neighbour);

}
