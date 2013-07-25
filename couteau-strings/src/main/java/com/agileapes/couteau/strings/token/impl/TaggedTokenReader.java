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

/**
 * This reader will allow for converting existing token readers into token readers with an assignable tag
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/23/13, 12:02 PM)
 */
public class TaggedTokenReader implements TokenReader {

    private final int tag;
    private final TokenReader reader;

    public TaggedTokenReader(int tag, TokenReader reader) {
        this.tag = tag;
        this.reader = reader;
    }

    @Override
    public Token read(String text) {
        final Token token = reader.read(text);
        return token == null ? null : new TaggedToken(token, tag);
    }

}
