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

import java.io.OutputStream;

/**
 * This is a shortcut for initializing a Java language output.
 * This could be replaced with an instance of {@code GenericLanguageOutput<Java>}
 * without any problems.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (5/20/13, 8:03 PM)
 */
public class JavaLanguageOutput extends GenericLanguageOutput<Java> {

    public JavaLanguageOutput(OutputStream outputStream) {
        super(outputStream);
    }

}
