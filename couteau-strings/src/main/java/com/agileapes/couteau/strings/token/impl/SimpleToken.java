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

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/23/13, 11:32 AM)
 */
public class SimpleToken implements Token {

    private final int tag;
    private final int start;
    private final int end;
    private final int margin;

    public SimpleToken(int start, int end) {
        this(start, end, NO_TAG);
    }

    public SimpleToken(int start, int end, int tag) {
        this(start, end, 0, tag);
    }

    public SimpleToken(int start, int end, int margin, int tag) {
        this.tag = tag;
        this.start = start;
        this.margin = margin;
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
    public int getMargin() {
        return margin;
    }

    @Override
    public int getLength() {
        return end - start;
    }

    @Override
    public boolean isTagged() {
        return tag != NO_TAG;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d,%d,%d)", start, end, margin, tag);
    }

}
