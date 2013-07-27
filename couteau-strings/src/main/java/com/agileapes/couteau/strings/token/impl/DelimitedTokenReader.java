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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/27, 14:57)
 */
public class DelimitedTokenReader implements TokenReader {

    private final Set<String> delimiters;
    private final boolean allowNonDelimited;

    public DelimitedTokenReader(Collection<String> delimiters) {
        this(false, delimiters);
    }

    public DelimitedTokenReader(String... delimiters) {
        this(false, delimiters);
    }

    public DelimitedTokenReader(boolean allowNonDelimited, Collection<String> delimiters) {
        this.allowNonDelimited = allowNonDelimited;
        this.delimiters = new HashSet<String>(delimiters);
    }

    public DelimitedTokenReader(boolean allowNonDelimited, String... delimiters) {
        this(allowNonDelimited, Arrays.asList(delimiters));
    }

    @Override
    public Token read(String text) {
        int position = 0;
        while (position < text.length()) {
            boolean found = false;
            for (String delimiter : delimiters) {
                if (text.substring(position).startsWith(delimiter)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
            position ++;
        }
        if (position >= text.length() && !allowNonDelimited) {
            return null;
        }
        return new SimpleToken(0, position);
    }
}
