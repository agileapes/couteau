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

package com.agileapes.couteau.xml.parse.impl;

import com.agileapes.couteau.xml.error.XmlParseError;
import com.agileapes.couteau.xml.node.XmlNode;
import com.agileapes.couteau.xml.parse.XmlParser;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * This implementation uses an event-driven, SAX parser to parse the XML. In this way, it should be faster than
 * {@link DomXmlParser} since it does not parse the entire XML document first, before converting it to a tree-representation
 * of the document.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/1/13, 1:48 PM)
 */
public class SaxXmlParser implements XmlParser {

    private final SAXParser parser;

    public SaxXmlParser() throws ParserConfigurationException, SAXException {
        parser = SAXParserFactory.newInstance().newSAXParser();
    }

    @Override
    public XmlNode parse(InputStream source) throws XmlParseError {
        final SaxEventHandler handler = new SaxEventHandler();
        try {
            parser.parse(source, handler);
            parser.getXMLReader().setProperty("http://xml.org/sax/properties/lexical-handler", handler);
        } catch (SAXException e) {
            throw new XmlParseError("Failed to parse input", e);
        } catch (IOException e) {
            throw new XmlParseError("Failed to read the input document", e);
        }
        return handler.getDocumentRoot();
    }

}