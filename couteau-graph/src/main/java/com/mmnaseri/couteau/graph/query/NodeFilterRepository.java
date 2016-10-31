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

package com.mmnaseri.couteau.graph.query;

import com.mmnaseri.couteau.graph.node.NodeFilter;
import com.mmnaseri.couteau.graph.query.filters.AssignableNodeFilter;
import com.mmnaseri.couteau.graph.query.filters.OriginNodeFilter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This is essentially a repository for all node filters that can be used by different pattern compilers
 * as the central location for all registered functions
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
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
        addFilter("origin", new OriginNodeFilter());
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
