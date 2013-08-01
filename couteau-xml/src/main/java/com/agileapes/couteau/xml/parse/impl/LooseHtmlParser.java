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
import org.w3c.tidy.Tidy;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class relies on the JTidy library's implementation of DOM parser to parse a loosely regulated HTML document.
 * For this class to work, you must have JTidy on your class path.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/29, 20:20)
 */
public class LooseHtmlParser extends AbstractXmlDocumentParser {

    /**
     * Instantiates the parser using the specified builder
     * @param builder    the document builder to be used for parsing
     * @throws ParserConfigurationException
     */
    public LooseHtmlParser(DocumentBuilder builder) throws ParserConfigurationException {
        super(builder);
    }

    /**
     * @see AbstractXmlDocumentParser#AbstractXmlDocumentParser()
     * @throws ParserConfigurationException
     */
    public LooseHtmlParser() throws ParserConfigurationException {
    }

    /**
     * This is the template method which is supposed to return the document instance that contains the root element
     * @param source    the source input stream
     * @return the document
     * @throws XmlParseError
     */
    @Override
    protected Document getDocument(InputStream source) throws XmlParseError {
        final Tidy tidy = new Tidy();
        tidy.setErrout(new PrintWriter(new StringWriter()));
        return tidy.parseDOM(source, new ByteArrayOutputStream());
    }

}
