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
import com.agileapes.couteau.lang.support.LanguageInput;

import java.io.Reader;

/**
 * This is a generic language input that can be used for almost any language,
 * providing access to a delegate reader
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (5/20/13, 8:03 PM)
 */
public class GenericLanguageInput<L extends Language> implements LanguageInput<L> {

    private final Reader reader;

    public GenericLanguageInput(Reader reader) {
        this.reader = reader;
    }

    @Override
    public Reader getReader() {
        return reader;
    }

}
