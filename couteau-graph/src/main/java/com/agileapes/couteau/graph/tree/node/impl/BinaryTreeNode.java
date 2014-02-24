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

package com.agileapes.couteau.graph.tree.node.impl;

import com.agileapes.couteau.basics.api.Stringifiable;

/**
 * This class implements the properties expected of a binary tree.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/25, 11:08)
 */
public class BinaryTreeNode<N extends BinaryTreeNode<N>> extends DirectedTreeNode<N> {
    
    private final Stringifiable<BinaryTreeNode<N>> stringifiable;

    public BinaryTreeNode() {
        this(null);
    }

    public BinaryTreeNode(Stringifiable<BinaryTreeNode<N>> stringifiable) {
        this.stringifiable = stringifiable;
    }

    public void setLeftChild(N node) {
        final N rightChild = getRightChild();
        removeAllChildren();
        addChild(node);
        addChild(rightChild);
    }

    public void setRightChild(N node) {
        final N leftChild = getLeftChild();
        removeAllChildren();
        addChild(leftChild);
        addChild(node);
    }

    public N getLeftChild() {
        if (getChildren().isEmpty()) {
            return null;
        }
        return getChildren().get(0);
    }

    public N getRightChild() {
        if (getChildren().size() < 2) {
            return null;
        }
        return getChildren().get(1);
    }

    @Override
    public void addNeighbor(N neighbor) {
        if (neighbor == null) {
            throw new IllegalArgumentException();
        }
        super.addNeighbor(neighbor);
    }

    @Override
    public void addChild(N child) {
        if (getChildren().size() >= 2) {
            throw new IllegalStateException();
        }
        super.addChild(child);
    }

    @Override
    public void setParent(N parent) {
        if (parent == null) {
            throw new IllegalArgumentException();
        }
        super.setParent(parent);
    }

    @Override
    public N getParent() {
        return super.getParent() == null ? null : super.getParent();
    }

    @Override
    public void removeChild(N child) {
        super.removeChild(child);
    }

    @Override
    public String toString() {
        if (stringifiable != null) {
            return stringifiable.toString(this);
        }
        return super.toString();
    }
}
