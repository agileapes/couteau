package com.agileapes.couteau.strings.token.impl;

import com.agileapes.couteau.strings.token.Token;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/23/13, 11:32 AM)
 */
public class SimpleToken implements Token {

    private final int tag;
    private final int start;
    private final int end;

    public SimpleToken(int start, int end) {
        this(start, end, NO_TAG);
    }

    public SimpleToken(int start, int end, int tag) {
        this.tag = tag;
        this.start = start;
        this.end = end;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public int getEnd() {
        return end;
    }

    @Override
    public int getLength() {
        return end - start;
    }

    @Override
    public boolean isTagged() {
        return tag != NO_TAG;
    }

}
