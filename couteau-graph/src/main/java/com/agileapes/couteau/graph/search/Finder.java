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

package com.agileapes.couteau.graph.search;

import com.agileapes.couteau.graph.node.Node;

import java.util.List;

/**
 * This interface encapsulates the process of finding a specific node within a graph.
 * The finder is expected to carry out the search for the given connected component.
 * If the desired nodes are within a different component, or if the nodes within this
 * component are not strongly connected, the node of origin might not yield the desired
 * nodes.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/24, 15:10)
 */
public interface Finder {

    /**
     * This method will take a node and start searching from that node for all nodes
     * that match with the given matcher
     * @return the list of all nodes matching the given description. This list is sorted
     * according to the order with which the nodes were first encountered by the finder.
     */
    List<Node> find();

}