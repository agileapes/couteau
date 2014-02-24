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

/**
 * A node pattern is an object capable of dispensing finders for a specific matching process. The pattern itself
 * holds semantic data as to what is called an <em>acceptable</em> node in a graph of nodes, and what is not.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 5:56)
 */
public interface NodePattern {

    /**
     * This method will create and return a finder object that is capable of looking up nodes in the graph
     * component accessible from the origin node that match the internally held description.
     * @param origin    the node of origin
     * @return a finder which is capable of finding matching nodes
     */
    <N extends Node<N>> Finder<N> finder(N origin);

}
