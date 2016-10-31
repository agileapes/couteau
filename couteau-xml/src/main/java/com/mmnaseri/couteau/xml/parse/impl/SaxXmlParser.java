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
import com.mmnaseri.couteau.xml.parse.XmlParser;
import org.xml.sax.SAXException;

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
 * @author Milad Naseri (mmnaseri@programmer.net)
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
