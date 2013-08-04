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

package com.agileapes.couteau.lang.support;

import java.io.OutputStream;

/**
 * A language output for supposed language {@code L} is a way for the compiler
 * to interface with the output environment and as such provide a clear output
 * to the compilation process.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (5/20/13, 7:59 PM)
 */
public interface LanguageOutput<L extends Language> {

    /**
     * @return the output stream that should interface with the physical representation
     * of the compilation output
     */
    OutputStream getOutputStream();

}
