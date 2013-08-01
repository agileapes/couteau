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

package com.agileapes.couteau.xml.node;

import com.agileapes.couteau.basics.api.Filter;
import org.w3c.dom.Node;

import static com.agileapes.couteau.basics.collections.CollectionWrapper.with;

/**
 *
 * This class adds a level of indirection between the framework and the DOM model of having constant values for node types.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/31/13, 11:52 AM)
 */
public enum NodeType {

    ELEMENT_NODE(Node.ELEMENT_NODE),
    ATTRIBUTE_NODE(Node.ATTRIBUTE_NODE),
    TEXT_NODE(Node.TEXT_NODE),
    CDATA_SECTION_NODE(Node.CDATA_SECTION_NODE),
    ENTITY_REFERENCE_NODE(Node.ENTITY_REFERENCE_NODE),
    ENTITY_NODE(Node.ENTITY_NODE),
    PROCESSING_INSTRUCTION_NODE(Node.PROCESSING_INSTRUCTION_NODE),
    COMMENT_NODE(Node.COMMENT_NODE),
    DOCUMENT_NODE(Node.DOCUMENT_NODE),
    DOCUMENT_TYPE_NODE(Node.DOCUMENT_TYPE_NODE),
    DOCUMENT_FRAGMENT_NODE(Node.DOCUMENT_FRAGMENT_NODE),
    NOTATION_NODE(Node.NOTATION_NODE);

    private final short numeric;

    private NodeType(short numeric) {
        this.numeric = numeric;
    }

    private short getNumeric() {
        return numeric;
    }

    public static NodeType getNodeType(final short nodeType) {
        //noinspection unchecked
        return with(values())
                .keep(new Filter<NodeType>() {
                    @Override
                    public boolean accepts(NodeType item) {
                        return item.getNumeric() == nodeType;
                    }
                }).first();
    }

}
