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

package com.mmnaseri.couteau.graph.node.impl;

import com.mmnaseri.couteau.basics.api.Stringifiable;
import com.mmnaseri.couteau.graph.node.MutableNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/7/24, 15:04)
 */
public class DirectedNode<N extends DirectedNode<N>> implements MutableNode<N> {

    /**
     * The stringifiable object associated with the node, to help facilitate
     * writing the node as a string value
     */
    private final Stringifiable<DirectedNode<N>> stringifiable;

    /**
     * The map containing all user data associated with the node
     */
    private final Map<String, Object> userData = new HashMap<String, Object>();

    /**
     * This nodes neighbors and link weights
     */
    private final Map<N, Double> neighbors = new HashMap<N, Double>();

    /**
     * The map of all attribute values
     */
    private final Map<String, String> attributes = new HashMap<String, String>();

    public DirectedNode() {
        this(null);
    }

    public DirectedNode(Stringifiable<DirectedNode<N>> stringifiable) {
        this.stringifiable = stringifiable;
    }

    /**
     * Will update the value of the given attribute. If this attribute has not been set
     * previously, it will be added to the node.
     * @param attributeName    the name of the attribute to be changed
     * @param value            the value of the attribute
     */
    @Override
    public void setAttribute(String attributeName, String value) {
        attributes.put(attributeName, value);
    }

    /**
     * This method adds a new neighbor to the node, with the weight being set to zero.
     * If the node is already a neighbor of this node, the weight of the connection between
     * the two will be set to zero.
     * @param neighbor    the node be added as a neighbor
     */
    @Override
    public void addNeighbor(N neighbor) {
        if (neighbors.containsKey(neighbor)) {
            neighbors.remove(neighbor);
        }
        neighbors.put(neighbor, 0d);
    }

    @Override
    public void removeNeighbor(N neighbor) {
        neighbors.remove(neighbor);
    }

    @Override
    public void removeAllNeighbors() {
        for (N node : neighbors.keySet()) {
            removeNeighbor(node);
        }
    }

    /**
     * Will change the weight of the connection between the two nodes. If no link exists
     * between the nodes, this method will not change anything.
     * @param neighbor    the neighboring node
     * @param weight      the new weight of the link
     */
    @Override
    public void setLinkWeight(N neighbor, double weight) {
        if (neighbors.containsKey(neighbor)) {
            neighbors.remove(neighbor);
            neighbors.put(neighbor, weight);
        }
    }

    /**
     * This method will set the user data with the given key and value
     * @param key      the key
     * @param value    the value
     */
    @Override
    public void setUserData(String key, Object value) {
        userData.put(key, value);
    }

    /**
     * This method returns a set of the names of all the attributes associated
     * and assigned with this node
     * @return the set of attribute names
     */
    @Override
    public Set<String> getAttributeNames() {
        return attributes.keySet();
    }

    /**
     * This method returns the value assigned to the given attribute for this node
     * @param attributeName    the name of the attribute being queried for
     * @return the value of the attribute, or {@code null} if no such attribute has
     * been set for this node.
     */
    @Override
    public String getAttribute(String attributeName) {
        return attributes.containsKey(attributeName) ? attributes.get(attributeName) : null;
    }

    /**
     * This method will return a set of all the neighbors adjacent to this node. Do
     * note that this does not necessarily bind nodes in a two-way relation. This is
     * to allow for modeling of directed graphs.
     * @return the set of nodes adjacent to this node.
     */
    @Override
    public Set<N> getNeighbors() {
        return neighbors.keySet();
    }

    /**
     * Returns the weight associated with the originating from this node to the
     * node being queried. If no link has been established between the two nodes,
     * the return value will be {@code 0}.
     * @param neighbour    the neighbor to be queried for
     * @return the weight of the link originating from this node and ending in the
     * neighboring node. If no such link exists, the value returned is {@code 0}.
     */
    @Override
    public double getLinkWeight(N neighbour) {
        return neighbors.containsKey(neighbour) ? neighbors.get(neighbour) : 0d;
    }

    /**
     * This method will query for user data associated with this node with the
     * given key
     * @param key    the key to the data
     * @return the user data if it exists, or {@code null} if not
     */
    @Override
    public Object getUserData(String key) {
        return userData.containsKey(key) ? userData.get(key) : null;
    }

    @Override
    public String toString() {
        if (stringifiable != null) {
            return stringifiable.toString(this);
        }
        return super.toString();
    }
    
}
