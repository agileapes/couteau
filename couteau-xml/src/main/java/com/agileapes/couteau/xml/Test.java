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

package com.agileapes.couteau.xml;

import com.agileapes.couteau.basics.assets.StringInputStream;
import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.xml.node.XmlNode;
import com.agileapes.couteau.xml.parse.XmlParser;
import com.agileapes.couteau.xml.parse.impl.LooseHtmlParser;
import com.agileapes.couteau.xml.print.XmlPrinter;
import com.agileapes.couteau.xml.query.XmlPattern;

import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 14:01)
 */
public class Test {

    public static void main(String[] args) throws Exception {
        final XmlParser parser = new LooseHtmlParser();
        final XmlNode node = parser.parse(new StringInputStream("<p>Hello <b>there!<div>are you okay</div> now?"));
        new XmlPrinter(System.out).print(node);
        final List<Node> nodes = XmlPattern.compile("p/b{assignable(from=java.lang.Object, to=123);}").finder(node).find();
        System.out.println("Found: " + nodes.size());
        for (Node found : nodes) {
            System.out.println(((XmlNode) found).getNodeName());
            System.out.println(((XmlNode) found).getNodeTypeName());
            System.out.println(((XmlNode) found).getNodeValue());
        }
    }

}
