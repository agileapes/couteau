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
import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.tree.node.TreeNode;

/**
 * This class implements the properties expected of a binary tree.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/25, 11:08)
 */
public class BinaryTreeNode extends DirectedTreeNode {
    
    private final Stringifiable<BinaryTreeNode> stringifiable;

    public BinaryTreeNode() {
        this(null);
    }

    public BinaryTreeNode(Stringifiable<BinaryTreeNode> stringifiable) {
        this.stringifiable = stringifiable;
    }

    public void setLeftChild(BinaryTreeNode node) {
        final BinaryTreeNode rightChild = getRightChild();
        removeAllChildren();
        addChild(node);
        addChild(rightChild);
    }

    public void setRightChild(BinaryTreeNode node) {
        final BinaryTreeNode leftChild = getLeftChild();
        removeAllChildren();
        addChild(leftChild);
        addChild(node);
    }

    public BinaryTreeNode getLeftChild() {
        if (getChildren().isEmpty()) {
            return null;
        }
        return (BinaryTreeNode) getChildren().get(0);
    }

    public BinaryTreeNode getRightChild() {
        if (getChildren().size() < 2) {
            return null;
        }
        return (BinaryTreeNode) getChildren().get(1);
    }

    @Override
    public void addNeighbor(Node neighbor) {
        if (!(neighbor instanceof BinaryTreeNode)) {
            throw new IllegalArgumentException();
        }
        super.addNeighbor(neighbor);
    }

    @Override
    public void addChild(TreeNode child) {
        if (child != null && !(child instanceof BinaryTreeNode)) {
            throw new IllegalArgumentException();
        }
        if (getChildren().size() >= 2) {
            throw new IllegalStateException();
        }
        super.addChild(child);
    }

    @Override
    public void setParent(TreeNode parent) {
        if (!(parent instanceof BinaryTreeNode)) {
            throw new IllegalArgumentException();
        }
        super.setParent(parent);
    }

    @Override
    public BinaryTreeNode getParent() {
        return super.getParent() == null ? null : (BinaryTreeNode) super.getParent();
    }

    @Override
    public void removeChild(TreeNode child) {
        if (child != null && !(child instanceof BinaryTreeNode)) {
            throw new IllegalArgumentException();
        }
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
