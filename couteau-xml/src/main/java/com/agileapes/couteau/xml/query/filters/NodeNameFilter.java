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

package com.agileapes.couteau.xml.query.filters;

import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.node.NodeFilter;
import com.agileapes.couteau.xml.node.XmlNode;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 13:47)
 */
public class NodeNameFilter implements NodeFilter {

    private final String nodeName;

    public NodeNameFilter(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public boolean accepts(Node item) {
        if (item instanceof XmlNode) {
            final XmlNode xmlNode = (XmlNode) item;
            return xmlNode.getNodeName().matches(nodeName);
        }
        return false;
    }
}