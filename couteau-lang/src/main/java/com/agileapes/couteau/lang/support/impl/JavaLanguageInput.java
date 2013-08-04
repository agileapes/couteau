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

package com.agileapes.couteau.lang.support.impl;

import com.agileapes.couteau.lang.support.IdentifiedLanguageInput;

import java.io.Reader;

/**
 * Java language input provides an identified way to access Java source. It is not
 * necessary that the reader encapsulated by this class be a physical interface to
 * a Java source code file. However, the input provided by the reader must be compatible
 * with the input expected by the compiler.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
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
