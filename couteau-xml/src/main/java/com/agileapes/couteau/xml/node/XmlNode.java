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
 * This class extends {@link DirectedTreeNode} to add properties peculiar to XML nodes.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 1:51)
 */
public class XmlNode extends DirectedTreeNode {

    private String nodeName;
    private String nodeValue;
    private NodeType nodeType;
    private String namespaceUri;
    private String prefix;
    private String namespace;

    public XmlNode() {
    }

    public XmlNode(Stringifiable<DirectedTreeNode> stringifiable) {
        super(stringifiable);
    }

    /**
     * @return the name of this node as specified in the parsed document. This includes any namespace prefixes
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Changes the name of this node
     * @param nodeName    the new name of the node
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
        namespace = nodeName.matches(".*:.*")? nodeName.substring(0, nodeName.indexOf(":")) : null;
    }

    /**
     * @return the value of this node
     */
    public String getNodeValue() {
        return nodeValue;
    }

    /**
     * Changes this node's value
     * @param nodeValue    the new value for the node
     */
    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    /**
     * @return the node type for the node
     */
    public NodeType getNodeType() {
        return nodeType;
    }

    /**
     * Changes this node's node type, using the values specified in {@link org.w3c.dom.Node}.
     * @param nodeType    the type of the node
     */
    public void setNodeType(short nodeType) {
        this.nodeType = NodeType.getNodeType(nodeType);
    }

    /**
     * @return this node's namespace URI
     */
    public String getNamespaceUri() {
        return namespaceUri;
    }

    /**
     * Changes this node's namespace URI
     * @param namespaceUri    the new URI
     */
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    /**
     * @return this node's prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Changes this node's prefix
     * @param prefix    the new prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return this node's namespace
     */
    public String getNamespace() {
        return namespace;
    }

}
