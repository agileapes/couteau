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

package com.mmnaseri.couteau.lang.support.impl;

import com.mmnaseri.couteau.lang.support.IdentifiedLanguageInput;

import java.io.Reader;

/**
 * Java language input provides an identified way to access Java source. It is not
 * necessary that the reader encapsulated by this class be a physical interface to
 * a Java source code file. However, the input provided by the reader must be compatible
 * with the input expected by the compiler.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (5/20/13, 8:01 PM)
 */
public class JavaLanguageInput extends GenericLanguageInput<Java> implements IdentifiedLanguageInput<Java> {

    private final String className;

    public JavaLanguageInput(String className, Reader reader) {
        super(reader);
        this.className = className;
    }

    @Override
    public String getIdentifier() {
        return className;
    }

}
