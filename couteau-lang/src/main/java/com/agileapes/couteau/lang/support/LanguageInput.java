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

import java.io.Reader;

/**
 * A LanguageInput for language {@code L} is an encapsulation for a source that contains
 * inputs understandable by compilers/interpreters dealing with {@code L}.
 *
 * To that end, each input provides a reader that can be used by the compiler/interpreter
 * as a means to interface with the physical end of the input chain
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (5/20/13, 7:59 PM)
 */
public interface LanguageInput<L extends Language> {

    /**
     * A reader that can be used to interface with the actual input.
     *
     * The reader is expected to, when called upon, be able to provide the compiler/interpreter
     * working in the input with sufficient tokens and source data such that the process can
     * succeed without problems on the part of the reader.
     *
     * @return the reader
     */
    Reader getReader();

}
