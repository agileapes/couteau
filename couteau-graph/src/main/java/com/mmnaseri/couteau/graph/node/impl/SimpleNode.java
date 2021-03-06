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

package com.mmnaseri.couteau.graph.node.impl;

import com.mmnaseri.couteau.basics.api.Stringifiable;
import com.mmnaseri.couteau.graph.node.Node;

/**
 * The simple node is an implementation of {@link Node} that allows for
 * simple (undirected) graphs to be modeled easily. The only thing is that
 * to ensure this works, all nodes within the graph should be instances of
 * {@link SimpleNode}.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/7/25, 10:05)
 */
public class SimpleNode<N extends SimpleNode<N>> extends DirectedNode<N> {
    
    private final Stringifiable<SimpleNode<N>> stringifiable;

    public SimpleNode() {
        this(null);
    }

    public SimpleNode(Stringifiable<SimpleNode<N>> stringifiable) {
        this.stringifiable = stringifiable;
    }

    @Override
    public void addNeighbor(N neighbor) {
        if (getNeighbors().contains(neighbor)) {
            return;
        }
        super.addNeighbor(neighbor);
        //noinspection unchecked
        neighbor.addNeighbor((N) this);
    }

    @Override
    public void removeNeighbor(N neighbor) {
        if (!getNeighbors().contains(neighbor)) {
            return;
        }
        //noinspection unchecked
        neighbor.removeNeighbor((N) this);
        super.removeNeighbor(neighbor);
    }

    @Override
    public String toString() {
        if (stringifiable != null) {
            return stringifiable.toString(this);
        }
        return super.toString();
    }
}
