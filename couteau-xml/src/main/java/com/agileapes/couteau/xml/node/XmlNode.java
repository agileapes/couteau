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
import com.agileapes.couteau.graph.tree.node.impl.DirectedTreeNode;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 1:51)
 */
public class XmlNode extends DirectedTreeNode {

    private String nodeName;
    private String nodeValue;
    private short nodeType;
    private String namespaceUri;
    private String prefix;

    public XmlNode() {
    }

    public XmlNode(Stringifiable<DirectedTreeNode> stringifiable) {
        super(stringifiable);
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    public short getNodeType() {
        return nodeType;
    }

    public void setNodeType(short nodeType) {
        this.nodeType = nodeType;
    }

    public String getNamespaceUri() {
        return namespaceUri;
    }

    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNamespace() {
        if (nodeName.matches(".*:.*")) {
            return nodeName.substring(0, nodeName.indexOf(":"));
        }
        return null;
    }

    public String getNodeTypeName() {
        if (nodeType == 1) {
            return "ELEMENT_NODE";
        } else if (nodeType == 2) {
            return "ATTRIBUTE_NODE";
        } else if (nodeType == 3) {
            return "TEXT_NODE";
        } else if (nodeType == 4) {
            return "CDATA_SECTION_NODE";
        } else if (nodeType == 5) {
            return "ENTITY_REFERENCE_NODE";
        } else if (nodeType == 6) {
            return "ENTITY_NODE";
        } else if (nodeType == 7) {
            return "PROCESSING_INSTRUCTION_NODE";
        } else if (nodeType == 8) {
            return "COMMENT_NODE";
        } else if (nodeType == 9) {
            return "DOCUMENT_NODE";
        } else if (nodeType == 10) {
            return "DOCUMENT_TYPE_NODE";
        } else if (nodeType == 11) {
            return "DOCUMENT_FRAGMENT_NODE";
        } else if (nodeType == 12) {
            return "NOTATION_NODE";
        } else {
            return "UNKNOWN";
        }
    }

}
