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

package com.agileapes.couteau.xml.parse.impl;

import com.agileapes.couteau.xml.node.NodeType;
import com.agileapes.couteau.xml.node.XmlNode;
import com.agileapes.couteau.xml.node.XmlNodeStringifiable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Stack;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/1/13, 1:51 PM)
 */
public class SaxEventHandler extends SaxEventHandlerAdapter {

    private final Stack<XmlNode> nodes = new Stack<XmlNode>();
    private StringBuilder builder = null;
    private NodeType textType = null;
    private boolean isCdata = false;
    private XmlNode documentRoot;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        flushText();
        final XmlNode node = getXmlNode(qName);
        node.setNamespaceUri(uri);
        node.setNodeType(NodeType.ELEMENT_NODE);
        for (int i = 0; i < attributes.getLength(); i ++) {
            node.setAttribute(attributes.getQName(i), attributes.getValue(i));
        }
        if (!nodes.isEmpty()) {
            nodes.peek().addChild(node);
        }
        nodes.push(node);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        flushText();
        final XmlNode pop = nodes.pop();
        if (nodes.isEmpty()) {
            documentRoot = pop;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        handleCharacters(ch, start, length, isCdata ? NodeType.CDATA_SECTION_NODE : NodeType.TEXT_NODE);
    }

    @Override
    public void startCDATA() throws SAXException {
        isCdata = true;
    }

    @Override
    public void endCDATA() throws SAXException {
        isCdata = false;
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {
        handleCharacters(ch, start, length, NodeType.COMMENT_NODE);
    }

    private XmlNode getXmlNode(String nodeName) {
        final XmlNode node = new XmlNode(new XmlNodeStringifiable());
        node.setNodeName(nodeName);
        return node;
    }

    public XmlNode getDocumentRoot() {
        return documentRoot;
    }

    public void handleCharacters(char[] characters, int start, int length, NodeType type) throws SAXException {
        handleString(new String(characters, start, length), type);
    }

    private void handleString(String string, NodeType nodeType) throws SAXException {
        if (builder == null) {
            builder = new StringBuilder(string);
            textType = nodeType;
        } else if (nodeType.equals(textType)) {
            builder.append(string);
        } else {
            flushText();
            handleString(string, nodeType);
        }
    }

    private void flushText() throws SAXException {
        if (textType == null || builder == null) {
            return;
        }
        final XmlNode node = getXmlNode(textType.equals(NodeType.TEXT_NODE) ? "#text" : (textType.equals(NodeType.CDATA_SECTION_NODE) ? "#cdata" : "#comment"));
        node.setNodeType(textType);
        node.setNodeValue(builder.toString());
        if (nodes.isEmpty()) {
            throw new SAXException("Text nodes cannot occur at the root");
        }
        nodes.peek().addChild(node);
        builder = null;
        textType = null;
    }

}
