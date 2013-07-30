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

package com.agileapes.couteau.xml.print;

import com.agileapes.couteau.graph.tree.node.TreeNode;
import com.agileapes.couteau.graph.tree.walk.TreeNodeProcessor;
import com.agileapes.couteau.graph.util.TreePrinter;
import com.agileapes.couteau.xml.node.XmlNode;

import java.io.PrintStream;
import java.util.Stack;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 1:02)
 */
public class XmlPrinter extends TreePrinter {

    public XmlPrinter(PrintStream output) {
        super(output);
    }

    @Override
    protected TreeNodeProcessor<TreeNode> getProcessor() {
        return new TreeNodeProcessor<TreeNode>() {

            private final Stack<TreeNode> stack = new Stack<TreeNode>();

            @Override
            public void processBeforeChildren(TreeNode node) {
                if (!((XmlNode) node).getNodeName().startsWith("#")) {
                    for (TreeNode item : stack) {
                        output.print("\t");
                    }
                }
                if (((XmlNode) node).getNodeType() == org.w3c.dom.Node.TEXT_NODE) {
                    output.print(((XmlNode) node).getNodeValue());
                    stack.push(node);
                    return;
                }
                output.print("<");
                output.print(((XmlNode) node).getNodeName());
                for (String name : node.getAttributeNames()) {
                    if (name.startsWith("#")) {
                        continue;
                    }
                    output.print(" " + name + "=\"" + node.getAttribute(name) + "\"");
                }
                if (node.isLeaf()) {
                    output.print("/");
                }
                output.print(">");
                if (node.isLeaf() || !((XmlNode) node.getFirstChild()).getNodeName().startsWith("#")) {
                    output.println();
                }
                stack.push(node);
            }

            @Override
            public void processAfterChildren(TreeNode node) {
                stack.pop();
                if (node.isLeaf()) {
                    return;
                }
                if (!((XmlNode) node.getLastChild()).getNodeName().startsWith("#")
                        && (node.getPreviousSibling() != null && !((XmlNode) node.getPreviousSibling()).getNodeName().startsWith("#"))
                        && !((XmlNode) node.getLastChild()).getNodeName().startsWith("#")) {
                    for (TreeNode item : stack) {
                        output.print("\t");
                    }
                }
                output.print("</");
                output.print(((XmlNode) node).getNodeName());
                output.print(">");
                if (!((XmlNode) node).getNodeName().startsWith("#")) {
                    output.println();
                }
            }
        };
    }

}
