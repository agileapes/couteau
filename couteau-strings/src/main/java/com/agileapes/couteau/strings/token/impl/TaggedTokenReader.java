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
