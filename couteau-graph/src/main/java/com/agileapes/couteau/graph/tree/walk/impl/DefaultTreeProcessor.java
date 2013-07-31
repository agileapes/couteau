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

package com.agileapes.couteau.graph.tree.walk.impl;

import com.agileapes.couteau.graph.tree.node.TreeNode;
import com.agileapes.couteau.graph.tree.node.impl.BinaryTreeNode;
import com.agileapes.couteau.graph.tree.walk.BinaryTreeNodeProcessor;
import com.agileapes.couteau.graph.tree.walk.TreeNodeProcessor;
import com.agileapes.couteau.graph.tree.walk.TreeProcessor;

/**
 * This is an implementation of the node processor that handles all processing operations in a recursive manner
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/27, 17:53)
 */
public class DefaultTreeProcessor<N extends TreeNode> implements TreeProcessor<N> {

    @SuppressWarnings("unchecked")
    @Override
    public void process(N origin, TreeNodeProcessor<N> processor) {
        processor.processBeforeChildren(origin);
        if (origin instanceof BinaryTreeNode && processor instanceof BinaryTreeNodeProcessor<?>) {
            BinaryTreeNode binaryTreeNode = (BinaryTreeNode) origin;
            final BinaryTreeNodeProcessor<BinaryTreeNode> nodeProcessor = (BinaryTreeNodeProcessor<BinaryTreeNode>) processor;
            process((N) binaryTreeNode.getLeftChild(), processor);
            nodeProcessor.processBetweenChildren(binaryTreeNode);
            process((N) binaryTreeNode.getRightChild(), processor);
        } else {
            for (TreeNode node : origin.getChildren()) {
                process((N) node, processor);
            }
        }
        processor.processAfterChildren(origin);
    }

}
