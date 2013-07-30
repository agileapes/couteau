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

package com.agileapes.couteau.graph.tree.node;

import com.agileapes.couteau.graph.node.Node;

import java.util.List;

/**
 * This interface adds metadata to a node peculiar to a tree node. Properties such as
 * being the root, being a leaf, and having a parent and children are considered to be
 * such general, abstract ideas representing the core being of a tree.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/25, 10:27)
 */
public interface TreeNode extends Node {

    /**
     * @return the parent node for this node, or {@code null} if this node is the
     * root node
     */
    TreeNode getParent();

    /**
     * @return this node's children. If this list is empty, it means that this node
     * is a leaf node in the tree.
     */
    List<TreeNode> getChildren();

    /**
     * @return Determines whether or not this node is the root of the tree. In a single
     * tree, only one node can be the root.
     */
    boolean isRoot();

    /**
     * Marks leaves in the tree.
     * @return {@code true} if this node is a leaf node in the tree.
     */
    boolean isLeaf();

    /**
     * Determines whether this is inside node. An inside node is a node that is the parent
     * to some nodes, and has a parent of its own.
     * @return {@code true} if this is an inside node.
     */
    boolean isInsideNode();

    /**
     * Adds a child to this node
     * @param child    the child to be added
     */
    void addChild(TreeNode child);

    /**
     * Removes the given child from this node's list of children
     * @param child    the child to be removed
     */
    void removeChild(TreeNode child);

    /**
     * Changes the parent node of this node
     * @param parent    the new parent
     */
    void setParent(TreeNode parent);

    /**
     * Removes all this nodes children
     */
    void removeAllChildren();

    /**
     * Returns the depth of this node in this tree
     * @return this node's depth relative to the root, with the root's depth
     * being {@code 0}.
     */
    int getDepth();

    /**
     * @return This node's first child, or {@code null} if this is a leaf node
     */
    TreeNode getFirstChild();

    /**
     * @return This node's last child, or {@code null} if this is a leaf node
     */
    TreeNode getLastChild();

    /**
     * @return This node's previous sibling, or {@code null} if this is the root node
     */
    TreeNode getPreviousSibling();

    /**
     * @return This node's next sibling, or {@code null} if this is the root node
     */
    TreeNode getNextSibling();

    /**
     * @return This node's zero-based index among its parent's children nodes,
     * or {@code null} if this is the root node
     */
    Integer getNodeIndex();

}
