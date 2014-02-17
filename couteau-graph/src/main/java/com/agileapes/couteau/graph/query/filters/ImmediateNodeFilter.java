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

package com.agileapes.couteau.graph.query.filters;

import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.node.NodeFilter;

/**
 * Accepts nodes that are immediately connected to the node of origin
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 5:45)
 */
public class ImmediateNodeFilter<N extends Node> implements NodeFilter<N>, OriginNodeAware<N> {

    private N origin;

    @Override
    public void setOrigin(N origin) {
        this.origin = origin;
    }

    @Override
    public boolean accepts(N item) {
        return origin.getNeighbors().contains(item);
    }

}
