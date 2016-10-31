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

package com.mmnaseri.couteau.xml.print;

import com.mmnaseri.couteau.graph.tree.node.TreeNode;
import com.mmnaseri.couteau.xml.node.NodeType;
import com.mmnaseri.couteau.xml.node.XmlNode;

import java.io.PrintStream;

/**
 * This class facilitates the process of printing out correctly indented XML documents to the output
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 1:02)
 */
public class XmlPrinter {

    private final PrintStream output;

    public XmlPrinter() {
        this(System.out);
    }

    public XmlPrinter(PrintStream output) {
        this.output = output;
    }

    private static boolean printInline(XmlNode node) {
        if (node == null) {
            return false;
        }
        for (TreeNode treeNode : node.getChildren()) {
            final XmlNode xmlNode = (XmlNode) treeNode;
            if (xmlNode.getNodeType().equals(NodeType.TEXT_NODE)) {
                return true;
            }
        }
        return printInline((XmlNode) node.getParent());
    }

    public void print(XmlNode node) {
        if (node == null) {
            return;
        }
        if (node.getNodeType().equals(NodeType.TEXT_NODE)) {
            if (!node.getNodeValue().trim().isEmpty()) {
                output.print(node.getNodeValue());
            }
            return;
        }
        if (node.getNodeType().equals(NodeType.COMMENT_NODE)) {
            output.print("<!--");
            if (node.getNodeValue().contains("\n")) {
                output.println();
            }
            output.print(node.getNodeValue());
            output.print("-->");
            if (node.getNodeValue().contains("\n")) {
                output.println();
            }
            return;
        }
        if (node.getNodeType().equals(NodeType.CDATA_SECTION_NODE)) {
            output.print("<![CDATA[");
            if (node.getNodeValue().contains("\n")) {
                output.println();
            }
            output.print(node.getNodeValue());
            output.print("]]>");
            if (node.getNodeValue().contains("\n")) {
                output.println();
            }
            return;
        }
        final int indent = node.getDepth();
        if (!printInline((XmlNode) node.getParent())) {
            for (int i = 0; i < indent; i ++) {
                output.print("\t");
            }
        }
        output.print("<" + node.getNodeName());
        for (String attribute : node.getAttributeNames()) {
            output.print(" " + attribute + "=\"");
            final String value = node.getAttribute(attribute);
            if (value != null) {
                output.print(value.replace("\"", "&quot;"));
            }
            output.print('"');
        }
        if (node.getChildren().isEmpty()) {
            output.print(" /");
        }
        output.print(">");
        if (!printInline(node)) {
            output.println();
        }
        for (TreeNode treeNode : node.getChildren()) {
            print((XmlNode) treeNode);
        }
        if (!node.getChildren().isEmpty()) {
            if (!printInline(node)) {
                for (int i = 0; i < indent; i ++) {
                    output.print("\t");
                }
            }
            output.print("</" + node.getNodeName() + ">");
            if (!printInline((XmlNode) node.getParent())) {
                output.println();
            }
        }
    }

}
