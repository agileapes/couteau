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
import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.search.Finder;
import com.agileapes.couteau.strings.document.DocumentReader;
import com.agileapes.couteau.strings.document.impl.DefaultDocumentReader;
import com.agileapes.couteau.strings.token.impl.ContainedTokenReader;
import com.agileapes.couteau.strings.token.impl.DelimitedTokenReader;
import com.agileapes.couteau.strings.token.impl.IdentifierTokenReader;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/25, 14:47)
 */
public class NodePattern {

    public static final Pattern WHITESPACE = Pattern.compile("\\w+");
    static final Set<ConfigurableNamedNodeFilter> NODE_FILTERS = new HashSet<ConfigurableNamedNodeFilter>();

    public static void addFilter(ConfigurableNamedNodeFilter filter) {
        NODE_FILTERS.add(filter);
    }

    public static NodePattern compile(String query) {
        final DocumentReader reader = new DefaultDocumentReader(query);
        final List<NodeQueryFilter> filters = new ArrayList<NodeQueryFilter>();
        while (reader.hasMore()) {
            final NodeQueryFilter filter = new NodeQueryFilter();
            String read = reader.expect("^[\\[\\{/\\*]", true);
            if (read.equals("*")) {
                filters.add(new NodeQueryFilter.WildCardFilter(false));
                reader.skip(WHITESPACE);
                if (reader.hasMore()) {
                    reader.expect("/", false);
                    continue;
                }
                break;
            }
            if ("/".equals(read)) {
                filter.setImmediate(true);
                read = reader.expect("^[\\[\\{\\*]", true);
                if (read.equals("*")) {
                    filters.add(new NodeQueryFilter.WildCardFilter(true));
                    reader.skip(WHITESPACE);
                    if (reader.hasMore()) {
                        reader.expect("/", false);
                        continue;
                    }
                    break;
                }
            }
            if ("[".equals(read)) {
                while (true) {
                    String attribute = reader.read(reader.expectToken(new DelimitedTokenReader(",", "=", "]")));
                    String value = null;
                    if (reader.has("=")) {
                        reader.read("=", false);
                        if (reader.has(",")) {
                            value = "";
                        } else {
                            value = reader.read(reader.expectToken(new ContainedTokenReader("'\"`", false)));
                        }
                    }
                    final String delimiter = reader.expect("[,\\]]", false);
                    filter.addAttribute(attribute, value);
                    if (delimiter.equals("]")) {
                        break;
                    }
                }
                reader.skip(WHITESPACE);
                if (!reader.hasMore()) {
                    filters.add(filter);
                    break;
                }
                if (reader.has("\\{")) {
                    read = "{";
                } else if (!reader.has("/")) {
                    throw new IllegalStateException();
                }
            }
            reader.skip(WHITESPACE);
            if (read.equals("{")) {
                while (true) {
                    final String function = reader.read(reader.expectToken(new IdentifierTokenReader()));
                    final Map<String, String> arguments = new HashMap<String, String>();
                    String delimiter = reader.expect("[\\(,\\}]", false);
                    if (delimiter.equals("(")) {
                        while (true) {
                            final String name = reader.read(reader.expectToken(new DelimitedTokenReader(",", "=", ")")));
                            String value = null;
                            String nameDelimiter = reader.expect("[=,\\)]", false);
                            if (nameDelimiter.equals("=")) {
                                value = reader.read(reader.expectToken(new ContainedTokenReader("'\"`", false)));
                                nameDelimiter = reader.expect("[,\\)]", false);
                            }
                            arguments.put(name, value);
                            if (nameDelimiter.equals(")")) {
                                break;
                            }
                        }
                        delimiter = reader.expect("[,\\}]", false);
                    }
                    filter.addFunction(function, arguments);
                    if (delimiter.equals("}")) {
                        break;
                    }
                }
            }
            reader.skip(WHITESPACE);
            if (reader.hasMore()) {
                reader.expect("/", false);
            }
            filters.add(filter);
        }
        return new NodePattern(filters);
    }

    private final List<NodeQueryFilter> matchers;

    public NodePattern(List<NodeQueryFilter> matchers) {
        this.matchers = matchers;
    }

    public Finder finder(Node origin) {
        return new NodeQueryFinder(origin, matchers);
    }

}
