/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mmnaseri.couteau.graph.node;

import com.mmnaseri.couteau.graph.query.GraphNodePattern;

import java.util.List;

/**
 * Searchable nodes are nodes that can be used as an originating point for a search
 * using a string query, as is explained under
 * {@link GraphNodePattern}
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/7/27, 12:45)
 */
public interface SearchableNode<N extends SearchableNode<N>> extends Node<N> {

    /**
     * This method will search the graph, originating the search at this node.
     * The search will be carried out according to {@link GraphNodePattern}
     * and a list of all nodes matching the pattern will be returned, sorted by the order
     * in which they were first encountered.
     * @param pattern    the pattern which will be compiled into a set of matchers
     * @return the list of matching nodes accessible from this node
     */
    List<N> find(String pattern);

    String getPath();

}
