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
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;

/**
 * This parser uses the strict DOM parser to parse the input XML document. This means that the input document must be
 * explicitly defined and cannot have any errors. As such, this parser is usually not safe for parsing HTML documents.
 * For that purpose, you should use {@link LooseHtmlParser}.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/29, 19:44)
 */
public class DomXmlParser extends AbstractXmlDocumentParser {


    /**
     * @see AbstractXmlDocumentParser#AbstractXmlDocumentParser()
     * @throws ParserConfigurationException
     */
    public DomXmlParser() throws ParserConfigurationException {
    }

    /**
     * Instantiates the parser using the specified builder
     * @param builder    the document builder to be used for parsing
     * @throws ParserConfigurationException
     */
    public DomXmlParser(DocumentBuilder builder) throws ParserConfigurationException {
        super(builder);
    }

    /**
     * This is the template method which is supposed to return the document instance that contains the root element
     * @param source    the source input stream
     * @return the document
     * @throws XmlParseError
     */
    @Override
    protected Document getDocument(InputStream source) throws XmlParseError {
        final Document document;
        try {
            document = getDocumentBuilder().parse(source);
        } catch (Exception e) {
            throw new XmlParseError("Failed to parse document", e);
        }
        return document;
    }

}
