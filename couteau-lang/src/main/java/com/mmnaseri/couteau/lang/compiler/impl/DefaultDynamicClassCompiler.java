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

package com.mmnaseri.couteau.lang.compiler.impl;

import com.mmnaseri.couteau.lang.compiler.DynamicClassCompiler;
import com.mmnaseri.couteau.lang.error.CompileException;
import com.mmnaseri.couteau.lang.support.impl.JavaLanguageInput;
import com.mmnaseri.couteau.lang.support.impl.JavaLanguageOutput;
import com.mmnaseri.couteau.reflection.cp.MappedClassLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (5/20/13, 8:21 PM)
 */
public class DefaultDynamicClassCompiler implements DynamicClassCompiler {

    private final SimpleJavaSourceCompiler compiler;

    public DefaultDynamicClassCompiler() {
        this(null);
    }

    public DefaultDynamicClassCompiler(ClassLoader parent) {
        ClassLoader classLoader = parent != null ? new MappedClassLoader(parent) : new MappedClassLoader();
        compiler = new SimpleJavaSourceCompiler(classLoader);
    }

    @Override
    public void setOption(SimpleJavaSourceCompiler.Option option, String value) {
        compiler.setOption(option, value);
    }

    @Override
    public void enableOption(SimpleJavaSourceCompiler.Option option) {
        compiler.enableOption(option);
    }

    @Override
    public void disableOption(SimpleJavaSourceCompiler.Option option) {
        compiler.disableOption(option);
    }

    @Override
    public Class<?> compile(String className, Reader reader) throws CompileException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        compiler.compile(new JavaLanguageInput(className, reader), new JavaLanguageOutput(outputStream));
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new CompileException("Failed to close the output written by the compiler", e);
        }
        try {
            return compiler.getClassLoader().findClass(className);
        } catch (ClassNotFoundException ignored) {
            return null; //This case will never occur
        }
    }

    @Override
    public MappedClassLoader getClassLoader() {
        return compiler.getClassLoader();
    }

}
