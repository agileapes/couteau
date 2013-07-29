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

package com.agileapes.couteau.graph.query;

import com.agileapes.couteau.graph.node.ConfigurableNamedNodeFilter;
import com.agileapes.couteau.graph.node.NamedNodeFilter;
import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.node.NodeFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/26, 11:03)
 */
class NodeQueryFilter implements NodeFilter {

    public static class WildCardFilter extends NodeQueryFilter {

        public WildCardFilter(boolean immediate) {
            setImmediate(immediate);
        }

        @Override
        public boolean accepts(Node item) {
            return !(isImmediate() && !origin.getNeighbors().contains(item));
        }

        @Override
        public String toString() {
            return (isImmediate() ? "/" : "") + "*";
        }

    }

    protected Node origin;
    private final Map<String, String> attributes = new HashMap<String, String>();
    private final Map<String, Map<String, String>> functions = new HashMap<String, Map<String, String>>();
    private boolean immediate;

    public boolean isImmediate() {
        return immediate;
    }

    @Override
    public boolean accepts(Node item) {
        if (isImmediate() && !origin.getNeighbors().contains(item)) {
            return false;
        }
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            boolean found = false;
            if (entry.getValue() == null) {
                //we just want to see if it has such a property
                for (String attribute : item.getAttributeNames()) {
                    if (attribute.matches(entry.getKey())) {
                        found = true;
                        break;
                    }
                }
            } else {
                for (String attribute : item.getAttributeNames()) {
                    if (attribute.matches(entry.getKey()) && item.getAttribute(attribute) != null && item.getAttribute(attribute).matches(entry.getValue())) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                return false;
            }
        }
        for (Map.Entry<String, Map<String, String>> entry : functions.entrySet()) {
            NamedNodeFilter filter = null;
            for (NamedNodeFilter namedNodeFilter : NodePattern.NODE_FILTERS) {
                if (namedNodeFilter.getName().equals(entry.getKey())) {
                    filter = namedNodeFilter;
                    break;
                }
            }
            if (filter == null) {
                throw new IllegalStateException();
            }
            if (filter instanceof ConfigurableNamedNodeFilter) {
                for (Map.Entry<String, String> argument : entry.getValue().entrySet()) {
                    ((ConfigurableNamedNodeFilter) filter).setAttribute(argument.getKey(), argument.getValue());
                }
            }
            if (!filter.accepts(item)) {
                return false;
            }
        }
        return true;
    }

    public NodeQueryFilter forOrigin(Node origin) {
        this.origin = origin;
        return this;
    }

    public void addAttribute(String attribute, String value) {
        attributes.put(attribute, value);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        if (isImmediate()) {
            builder.append("/");
        }
        builder.append("[");
        int i = 0;
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            builder.append(entry.getKey());
            if (entry.getValue() != null) {
                builder.append("=").append(entry.getValue());
            }
            if (++ i < attributes.size()) {
                builder.append(",");
            }
        }
        builder.append("]{");
        i = 0;
        for (Map.Entry<String, Map<String, String>> entry : functions.entrySet()) {
            builder.append(entry.getKey()).append("(");
            int j = 0;
            for (Map.Entry<String, String> argument : entry.getValue().entrySet()) {
                builder.append(argument.getKey()).append("=").append(argument.getValue());
                if (++ j < entry.getValue().size()) {
                    builder.append(",");
                }
            }
            builder.append(")");
            if (++ i < functions.size()) {
                builder.append(",");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public void addFunction(String function, Map<String, String> arguments) {
        functions.put(function, arguments);
    }

}
