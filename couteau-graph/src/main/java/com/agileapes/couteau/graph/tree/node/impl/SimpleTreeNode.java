/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
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

package com.agileapes.couteau.graph.tree.node.impl;

import com.mmnaseri.couteau.basics.api.Stringifiable;
import com.agileapes.couteau.graph.tree.node.TreeNode;

/**
 * This is an extension to the {@link DirectedTreeNode} adding the property that
 * all edges become bidirectional.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/25, 10:14)
 */
public class SimpleTreeNode<N extends SimpleTreeNode<N>> extends DirectedTreeNode<N> implements TreeNode<N> {
    
    private final Stringifiable<SimpleTreeNode<N>> stringifiable;

    public SimpleTreeNode() {
        this(null);
    }

    public SimpleTreeNode(Stringifiable<SimpleTreeNode<N>> stringifiable) {
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
    public String toString() {
        if (stringifiable != null) {
            return stringifiable.toString(this);
        }
        return super.toString();
    }

}
