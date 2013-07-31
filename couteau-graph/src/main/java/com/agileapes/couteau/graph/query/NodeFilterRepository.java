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

import com.agileapes.couteau.graph.node.NodeFilter;
import com.agileapes.couteau.graph.query.filters.AssignableNodeFilter;

import java.util.*;

/**
 * This is essentially a repository for all node filters that can be used by different pattern compilers
 * as the central location for all registered functions
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 13:51)
 */
public abstract class NodeFilterRepository {

    /**
     * This map holds filters by name. Note that multiple names can be assigned to a single
     * filter
     */
    private static final Map<String, NodeFilter> NODE_FILTERS = new HashMap<String, NodeFilter>();

    /**
     * Loads the default filters
     */
    static {
        addFilter("assignable", new AssignableNodeFilter());
    }

    /**
     * Adds a new filter to the repository
     * @param name      the name of the filter
     * @param filter    the configured filter
     */
    public static void addFilter(String name, NodeFilter filter) {
        NODE_FILTERS.put(name, filter);
    }

    /**
     * @return the map of filters as available to the repository
     */
    public static Map<String, NodeFilter> getFilters() {
        return Collections.unmodifiableMap(NODE_FILTERS);
    }


}
