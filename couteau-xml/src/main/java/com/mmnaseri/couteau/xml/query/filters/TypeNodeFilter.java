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

package com.mmnaseri.couteau.xml.query.filters;

import com.mmnaseri.couteau.graph.node.ConfigurableNodeFilter;
import com.mmnaseri.couteau.graph.node.Node;
import com.mmnaseri.couteau.xml.node.NodeType;
import com.mmnaseri.couteau.xml.node.XmlNode;

import static com.mmnaseri.couteau.basics.collections.CollectionWrapper.with;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/31/13, 11:43 AM)
 */
public class TypeNodeFilter<N extends Node> implements ConfigurableNodeFilter<N> {

    private NodeType nodeType = null;

    @Override
    public void setAttribute(String name, String value) {
        if (with("type", "0").has(name)) {
            nodeType = Enum.valueOf(NodeType.class, value.toUpperCase());
        }
    }

    @Override
    public boolean accepts(N item) {
        return nodeType == null || item instanceof XmlNode && ((XmlNode) item).getNodeType().equals(nodeType);
    }

}
