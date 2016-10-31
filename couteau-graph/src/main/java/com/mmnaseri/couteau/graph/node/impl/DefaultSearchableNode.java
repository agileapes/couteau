/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
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

package com.mmnaseri.couteau.graph.node.impl;

import com.mmnaseri.couteau.basics.api.Transformer;
import com.mmnaseri.couteau.graph.node.Node;
import com.mmnaseri.couteau.graph.node.SearchableNode;
import com.mmnaseri.couteau.graph.query.GraphNodePattern;
import com.mmnaseri.couteau.graph.tree.node.TreeNode;

import java.util.List;
import java.util.Set;

import static com.mmnaseri.couteau.basics.collections.CollectionWrapper.with;

/**
 * This is essentially a wrapper class that allows for normal nodes to be
 * made searchable through {@link #find(String)}. All other methods are
 * delegates to the original node's methods.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/7/27, 12:50)
 */
public class DefaultSearchableNode implements SearchableNode<DefaultSearchableNode> {

    private final Node node;

    public DefaultSearchableNode(Node node) {
        this.node = node;
    }

    public Node<?> getNode() {
        return node;
    }

    @Override
    public List<DefaultSearchableNode> find(String pattern) {
        return GraphNodePattern.compile(pattern).finder((this)).find();
    }

    @Override
    public String getPath() {
        if (node instanceof TreeNode) {
            TreeNode treeNode = (TreeNode) node;
            final StringBuilder builder = new StringBuilder();
            if (treeNode.isRoot()) {
                return "{origin();}";
            }
            final SearchableNode<?> parent;
            if (treeNode.getParent() instanceof SearchableNode<?>) {
                parent = (SearchableNode) treeNode.getParent();
            } else {
                parent = new DefaultSearchableNode(treeNode.getParent());
            }
            builder.append(parent.getPath());
            builder.append("//");
            builder.append("#").append(treeNode.getNodeIndex());
            return builder.toString();
        }
        throw new UnsupportedOperationException("Path generation is only supported for tree nodes");
    }

    @Override
    public Set<String> getAttributeNames() {
        //noinspection unchecked
        return node.getAttributeNames();
    }

    @Override
    public String getAttribute(String attributeName) {
        return node.getAttribute(attributeName);
    }

    @Override
    public Set<DefaultSearchableNode> getNeighbors() {
        //noinspection unchecked
        return with(node.getNeighbors())
                .transform(new Transformer<Node, DefaultSearchableNode>() {
                    @Override
                    public DefaultSearchableNode map(Node input) {
                        //noinspection unchecked
                        return new DefaultSearchableNode(input);
                    }
                }).set();
    }

    @Override
    public double getLinkWeight(DefaultSearchableNode neighbour) {
        //noinspection unchecked
        return node.getLinkWeight(neighbour.getNode());
    }

    @Override
    public Object getUserData(String key) {
        return node.getUserData(key);
    }

    @Override
    public String toString() {
        return "searchable[" + node + "]";
    }
}
