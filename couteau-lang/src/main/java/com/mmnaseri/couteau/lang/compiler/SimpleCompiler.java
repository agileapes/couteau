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

package com.mmnaseri.couteau.lang.compiler;

import com.mmnaseri.couteau.lang.error.CompileException;
import com.mmnaseri.couteau.lang.support.Language;
import com.mmnaseri.couteau.lang.support.LanguageInput;
import com.mmnaseri.couteau.lang.support.LanguageOutput;

/**
 * The SimpleCompiler provides an interface for compiling data from one language
 * into another. Both the source and the target languages must be extensions to
 * {@link Language} as a way of signifying that there is support for that language
 * within this framework.
 *
 * To interface available source-code compilers, see {@link SimpleSourceCompiler}
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (5/20/13, 7:53 PM)
 */
public interface SimpleCompiler<S extends Language, T extends Language> {

    /**
     * The compile method is the central implementation of the work this class
     * is supposed to deliver. It should implement the whole process of reading
     * input from the source language input provider, understanding and compiling
     * that code, and then writing the output &mdash; in the correct format &mdash;
     * into the output provider for the target language
     * @param source    the input provider for the source language
     * @param target    the output interface for the target language
     * @throws CompileException if anything goes wrong in the process of compiling the code
     */
    void compile(LanguageInput<S> source, LanguageOutput<T> target) throws CompileException;

}
