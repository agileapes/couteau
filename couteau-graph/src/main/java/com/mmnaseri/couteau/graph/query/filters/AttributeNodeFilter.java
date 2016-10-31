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

package com.mmnaseri.couteau.graph.query.filters;

import com.mmnaseri.couteau.graph.node.Node;
import com.mmnaseri.couteau.graph.node.NodeFilter;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>This filter will take in patterns for attribute name and value and accept matching nodes. Matching is decided
 * thus:</p>
 * <ul>
 *     <li>If no attributes whose name matches the given pattern appears, the node is rejected</li>
 *     <li>If no value pattern has been supplied, the mere appearance of a matching attribute signals
 *     an accepted node</li>
 *     <li>Once the node has a attribute-value pair matching the given patterns, the node will be accepted</li>
 * </ul>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/7/30, 5:48)
 */
public class AttributeNodeFilter<N extends Node<N>> implements NodeFilter<N> {

    private final String attribute;
    private final String value;

    public AttributeNodeFilter(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public boolean accepts(N item) {
        final Set<String> target = new HashSet<String>();
        for (String attributeName : item.getAttributeNames()) {
            if (attributeName.matches(attribute)) {
                if (value == null) {
                    return true;
                }
                target.add(attributeName);
            }
        }
        if (target.isEmpty()) {
            return false;
        }
        for (String attributeName : target) {
            if (item.getAttribute(attributeName) != null && item.getAttribute(attributeName).matches(value)) {
                return true;
            }
        }
        return false;
    }

}
