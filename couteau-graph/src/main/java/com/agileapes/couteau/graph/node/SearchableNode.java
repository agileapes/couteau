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

import java.util.List;

/**
 * Searchable nodes are nodes that can be used as an originating point for a search
 * using a string query, as is explained under
 * {@link com.agileapes.couteau.graph.query.GraphNodePattern}
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/27, 12:45)
 */
public interface SearchableNode<N extends SearchableNode<N>> extends Node {

    /**
     * This method will search the graph, originating the search at this node.
     * The search will be carried out according to {@link com.agileapes.couteau.graph.query.GraphNodePattern}
     * and a list of all nodes matching the pattern will be returned, sorted by the order
     * in which they were first encountered.
     * @param pattern    the pattern which will be compiled into a set of matchers
     * @return the list of matching nodes accessible from this node
     */
    List<N> find(String pattern);

}
