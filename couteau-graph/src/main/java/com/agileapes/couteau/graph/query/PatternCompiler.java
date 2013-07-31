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

import java.util.List;

/**
 * This interface encapsulates the process of compiling a string pattern into a list of filters that, when
 * combined together, will only accept nodes described by the given pattern.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 5:56)
 */
public interface PatternCompiler {

    /**
     * This method compiles the given pattern. The semantics of the pattern itself are implementation-specific.
     * @param pattern    the pattern to be compiled
     * @return the list of filters that are equivalent with the pattern
     */
    List<NodeQueryFilter> compile(String pattern);

}
