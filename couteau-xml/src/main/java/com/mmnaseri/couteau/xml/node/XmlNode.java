/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
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

package com.mmnaseri.couteau.xml.node;

import com.mmnaseri.couteau.basics.api.Stringifiable;
import com.mmnaseri.couteau.graph.node.SearchableNode;
import com.mmnaseri.couteau.graph.tree.node.impl.DirectedTreeNode;
import com.mmnaseri.couteau.xml.query.XmlPattern;

import java.util.List;

/**
 * This class extends {@link DirectedTreeNode} to add properties peculiar to XML nodes.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/7/30, 1:51)
 */
public class XmlNode extends DirectedTreeNode<XmlNode> implements SearchableNode<XmlNode> {

    private final Stringifiable<XmlNode> stringifiable;
    private String nodeName;
    private String nodeValue;
    private NodeType nodeType;
    private String namespaceUri;
    private String prefix;
    private String namespace;

    public XmlNode() {
        this(null);
    }

    public XmlNode(Stringifiable<XmlNode> stringifiable) {
        this.stringifiable = stringifiable;
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
        setNodeType(NodeType.getNodeType(nodeType));
    }

    /**
     * Changes this node's node type, using the values specified in {@link org.w3c.dom.Node}.
     * @param nodeType    the type of the node
     */
    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
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

    @Override
    public String toString() {
        return stringifiable == null ? super.toString() : stringifiable.toString(this);
    }

    @Override
    public List<XmlNode> find(String pattern) {
        return XmlPattern.compile(pattern).finder(this).find();
    }

    @Override
    public String getPath() {
        return isRoot() ? "{origin();}" : getParent().getPath() + "//#" + getNodeIndex();
    }

}
