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

package com.mmnaseri.couteau.graph.tree.walk.impl;

import com.mmnaseri.couteau.graph.tree.node.TreeNode;
import com.mmnaseri.couteau.graph.tree.node.impl.BinaryTreeNode;
import com.mmnaseri.couteau.graph.tree.walk.BinaryTreeNodeProcessor;
import com.mmnaseri.couteau.graph.tree.walk.TreeNodeProcessor;
import com.mmnaseri.couteau.graph.tree.walk.TreeProcessor;

/**
 * This is an implementation of the node processor that handles all processing operations in a recursive manner
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/7/27, 17:53)
 */
public class DefaultTreeProcessor<N extends TreeNode<N>> implements TreeProcessor<N> {

    @SuppressWarnings("unchecked")
    @Override
    public void process(N origin, TreeNodeProcessor<N> processor) {
        processor.processBeforeChildren(origin);
        if (origin instanceof BinaryTreeNode<?> && processor instanceof BinaryTreeNodeProcessor<?>) {
            BinaryTreeNode binaryTreeNode = (BinaryTreeNode) origin;
            final BinaryTreeNodeProcessor nodeProcessor = (BinaryTreeNodeProcessor) processor;
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
