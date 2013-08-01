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

import com.agileapes.couteau.xml.parse.XmlParser;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/1/13, 4:53 PM)
 */
public class XmlParserFactory {

    private static XmlParserFactory ourInstance = new XmlParserFactory();

    public static XmlParserFactory getInstance() {
        return ourInstance;
    }

    private XmlParserFactory() {
    }

    public XmlParser newParser() throws Exception {
        return new SaxXmlParser();
    }

}
