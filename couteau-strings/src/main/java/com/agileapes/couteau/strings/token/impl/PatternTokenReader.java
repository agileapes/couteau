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

package com.agileapes.couteau.strings.token.impl;

import com.agileapes.couteau.strings.token.Token;
import com.agileapes.couteau.strings.token.TokenReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/23/13, 1:16 PM)
 */
public class PatternTokenReader implements TokenReader {

    private final Pattern pattern;

    public PatternTokenReader(Pattern pattern) {
        this.pattern = pattern;
    }

    public PatternTokenReader(String pattern) {
        this.pattern = Pattern.compile(pattern.startsWith("^") ? pattern : pattern, Pattern.DOTALL | Pattern.MULTILINE);
    }

    @Override
    public Token read(String text) {
        final Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return new SimpleToken(matcher.start(), matcher.end());
        }
        return null;
    }
}
