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

package com.agileapes.couteau.lang.compiler;

import com.agileapes.couteau.lang.compiler.impl.SimpleJavaSourceCompiler;
import com.agileapes.couteau.lang.error.CompileException;
import com.agileapes.couteau.reflection.cp.MappedClassLoader;

import java.io.Reader;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (5/20/13, 8:20 PM)
 */
public interface DynamicClassCompiler {

    /**
     * This method encapsulates the whole process of compilation
     * @param className    the name of the class that is being compiled. This identifier is known
     *                     externally to the context from which this method is called, since prior
     *                     to compiling the source code there is no way of knowing this identifier.
     * @param reader       the reader which exposes the given source code to the compiler
     * @return the compiled class object
     * @throws com.agileapes.couteau.lang.error.CompileException
     */
    Class<?> compile(String className, Reader reader) throws CompileException;

    /**
     * This method exposes the class loader that is being used by the current instance of the compiler
     * @return the class loader
     */
    MappedClassLoader getClassLoader();

    /**
     * This sets the option values for the underlying Java compiler
     * @param option    the option
     * @param value     the desired value
     */
    void setOption(SimpleJavaSourceCompiler.Option option, String value);

    /**
     * This enables the given option, if it's currently disabled.
     * @param option    the option
     */
    void enableOption(SimpleJavaSourceCompiler.Option option);

    /**
     * This disables the given option, if it's currently enabled.
     * @param option    the option
     */
    void disableOption(SimpleJavaSourceCompiler.Option option);


}
