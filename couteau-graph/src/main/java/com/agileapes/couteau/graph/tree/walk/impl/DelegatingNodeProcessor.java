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

package com.agileapes.couteau.graph.tree.walk.impl;

import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.graph.tree.node.TreeNode;
import com.agileapes.couteau.graph.tree.walk.TreeNodeProcessor;

/**
 * This is a node processor that has a single {@link #process(Object)} method that will be called
 * whenever required
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/27, 17:57)
 */
public abstract class DelegatingNodeProcessor<N extends TreeNode> implements TreeNodeProcessor<N>, Processor<N> {

    @Override
    public void processBeforeChildren(N node) {
    }

    @Override
    public void processAfterChildren(N node) {
    }

}
