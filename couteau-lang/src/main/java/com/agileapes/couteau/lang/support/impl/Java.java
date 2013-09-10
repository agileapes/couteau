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

import com.agileapes.couteau.lang.compiler.SimpleCompiler;
import com.agileapes.couteau.lang.compiler.impl.SimpleJavaSourceCompiler;
import com.agileapes.couteau.lang.support.Language;

/**
 * This is the internal representation of the Java&trade; language and can be used
 * to get access to various compilation and other language facilities available through
 * this framework
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (5/20/13, 7:59 PM)
 */
public class Java extends Language {

    /**
     * This is used to get instances of the candidate compiler as designated by
     * the framework designer
     * @return the candidate compiler
     */
    public static SimpleCompiler<Java, Java> getCandidateCompiler() {
        return new SimpleJavaSourceCompiler(ClassLoader.getSystemClassLoader());
    }

}
