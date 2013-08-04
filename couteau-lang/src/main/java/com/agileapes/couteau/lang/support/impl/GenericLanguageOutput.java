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

import com.agileapes.couteau.lang.support.Language;
import com.agileapes.couteau.lang.support.LanguageOutput;

import java.io.OutputStream;

/**
 * This is a generic language output that can be used to give access to a target
 * output stream for any language
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (5/20/13, 8:04 PM)
 */
public class GenericLanguageOutput<L extends Language> implements LanguageOutput<L> {

    private final OutputStream outputStream;

    public GenericLanguageOutput(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

}
