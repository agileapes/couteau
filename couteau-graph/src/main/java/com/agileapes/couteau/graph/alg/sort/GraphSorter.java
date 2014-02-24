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

package com.agileapes.couteau.graph.alg.sort;

import com.agileapes.couteau.graph.node.Node;

import java.util.Collection;
import java.util.List;

/**
 * This interface abstracts the process of taking an entry point into the graph,
 * and then sorting all reachable nodes based on a certain criteria.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/29, 15:41)
 */
public interface GraphSorter<T extends Node<T>> {

    /**
     * This method sorts the given component, returning the nodes in the component
     * in the ordering which they have received while being sorted
     * @param nodes    the collection of nodes being sorted
     * @return sorted nodes
     */
    List<T> sort(Collection<T> nodes);

}
