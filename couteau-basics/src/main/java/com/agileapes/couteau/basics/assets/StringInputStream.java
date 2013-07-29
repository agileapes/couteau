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

package com.agileapes.couteau.basics.assets;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class allows for redirection of a String byte array to be used as
 * an input stream.
 *
 * The point of operation is very simple, as it calls to {@link String#getBytes()}.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 0:03)
 */
public class StringInputStream extends InputStream {

    private final byte[] input;
    private int cursor;

    public StringInputStream(String input) {
        this.input = input.getBytes();
        this.cursor = 0;
    }

    @Override
    public int read() throws IOException {
        return cursor < input.length ? input[cursor ++] : -1;
    }

}
