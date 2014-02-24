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

package com.agileapes.couteau.xml.node;

import com.agileapes.couteau.basics.api.Stringifiable;
import com.agileapes.couteau.graph.tree.node.TreeNode;

import java.util.List;

import static com.agileapes.couteau.basics.collections.CollectionWrapper.with;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/1/13, 2:19 PM)
 */
public class XmlNodeStringifiable implements Stringifiable<XmlNode> {
    @Override
    public String toString(XmlNode node) {
        final StringBuilder builder = new StringBuilder();
        builder.append(node.getNodeName()).append("[");
        int i = 0;
        for (String name : node.getAttributeNames()) {
            if (i ++ > 0) {
                builder.append(",");
            }
            builder.append("@").append(name);
            builder.append("=").append(node.getAttribute(name));
        }
        builder.append("]{");
        final List<XmlNode> children = node.getChildren();
        for (i = 0; i < children.size(); i++) {
            final TreeNode child = children.get(i);
            if (i > 0) {
                builder.append(",");
            }
            builder.append(child == null ? "(x)" : child);
        }
        builder.append("}");
        if (with(NodeType.TEXT_NODE, NodeType.COMMENT_NODE, NodeType.CDATA_SECTION_NODE).has(node.getNodeType())) {
            builder.append("{").append(node.getNodeValue()).append("}");
        }
        return builder.toString();
    }

}
