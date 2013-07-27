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

package com.agileapes.couteau.graph.util;

import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.graph.tree.node.TreeNode;
import com.agileapes.couteau.graph.tree.traverse.TreeTraverse;
import com.agileapes.couteau.graph.tree.traverse.impl.PreOrderTreeTraverse;

import java.io.PrintStream;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/25, 11:15)
 */
public class TreePrinter {

    private final PrintStream output;
    private final NodePrinter nodePrinter;
    private final TreeTraverse traverse;

    public TreePrinter(PrintStream output, NodePrinter nodePrinter) {
        this.output = output;
        this.nodePrinter = nodePrinter;
        this.traverse = new PreOrderTreeTraverse();
    }

    public void print(TreeNode root) {
        traverse.traverse(root, new Processor<TreeNode>() {
            @Override
            public void process(TreeNode input) {
                if (input == null) {
                    output.println();
                    return;
                }
                final int indent = input.getDepth();
                for (int i = 0; i < indent; i ++) {
                    output.print("\t");
                }
                nodePrinter.print(input, output);
                output.println();
            }
        });
    }

}
