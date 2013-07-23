package com.agileapes.couteau.strings.token.impl;

import com.agileapes.couteau.strings.token.Token;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/23/13, 12:03 PM)
 */
public class TaggedToken implements Token {

    private final Token token;
    private final int tag;

    public TaggedToken(Token token) {
        this(token, NO_TAG);
    }

    public TaggedToken(Token token, int tag) {
        this.token = token;
        this.tag = tag;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public int getStart() {
        return token.getStart();
    }

    @Override
    public int getEnd() {
        return token.getEnd();
    }

    @Override
    public int getLength() {
        return token.getLength();
    }

    @Override
    public boolean isTagged() {
        return tag != NO_TAG;
    }

}
