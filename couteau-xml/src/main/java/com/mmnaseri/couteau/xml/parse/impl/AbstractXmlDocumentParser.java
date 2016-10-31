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

package com.mmnaseri.couteau.xml.parse.impl;

import com.mmnaseri.couteau.xml.error.XmlParseError;
import com.mmnaseri.couteau.xml.node.XmlNode;
import com.mmnaseri.couteau.xml.node.XmlNodeStringifiable;
import com.mmnaseri.couteau.xml.parse.XmlParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;

/**
 * <p>This parser uses a standard W3C document builder to generate a dom model of the given XML instance.</p>
 *
 * <p>The actual process of using the document builder to parse the document is left to the extending classes.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (8/1/13, 1:19 PM)
 */
public abstract class AbstractXmlDocumentParser implements XmlParser {

    private final DocumentBuilder builder;

    /**
     * Instantiates the parser with the the default document builder
     * @see DocumentBuilderFactory#newDocumentBuilder()
     * @throws ParserConfigurationException
     */
    public AbstractXmlDocumentParser() throws ParserConfigurationException {
        this(DocumentBuilderFactory.newInstance().newDocumentBuilder());
    }

    /**
     * Instantiates the parser using the specified builder
     * @param builder    the document builder to be used for parsing
     * @throws ParserConfigurationException
     */
    public AbstractXmlDocumentParser(DocumentBuilder builder) throws ParserConfigurationException {
        this.builder = builder;
    }

    /**
     * <p>This method will take in an input stream and reading the XML content it points to, will return a
     * tree representation of the given XML document.</p>
     *
     * <p>Because the tree representation adds much metadata to the XML document, it might be unwise to use
     * this facility for XML documents that are considered <em>too large</em>. Just how large this means, is
     * up to resources and careful benchmarking of the tool on the target machine.</p>
     * @param source    the input steam which will present the XML document
     * @return a parsed, tree representation of the tree
     * @throws XmlParseError
     */
    @Override
    public XmlNode parse(InputStream source) throws XmlParseError {
        return parseNode(getDocument(source).getDocumentElement());
    }

    /**
     * This is the template method which is supposed to return the document instance that contains the root element
     * @param source    the source input stream
     * @return the document
     * @throws XmlParseError
     */
    protected abstract Document getDocument(InputStream source) throws XmlParseError;

    /**
     * @return the document builder associated with the class
     */
    protected DocumentBuilder getDocumentBuilder() {
        return builder;
    }

    /**
     * This method will recursively process the root node to generate the tree model representation of the
     * XML document rooted at that node
     * @param node    the node to be processed
     * @return the processed tree model
     */
    private static XmlNode parseNode(Node node) {
        final XmlNode treeNode = new XmlNode(new XmlNodeStringifiable());
        treeNode.setNodeName(node.getNodeName());
        treeNode.setNodeValue(node.getNodeValue());
        treeNode.setNamespaceUri(node.getNamespaceURI());
        treeNode.setPrefix(node.getPrefix());
        treeNode.setNodeType(node.getNodeType());
        final NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i ++) {
                final Node item = attributes.item(i);
                treeNode.setAttribute(item.getNodeName(), item.getNodeValue());
            }
        }
        final NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i ++) {
            treeNode.addChild(parseNode(children.item(i)));
        }
        return treeNode;
    }

}
