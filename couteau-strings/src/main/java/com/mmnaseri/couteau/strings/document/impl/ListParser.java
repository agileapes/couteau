/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mmnaseri.couteau.strings.document.impl;

import com.mmnaseri.couteau.strings.document.DocumentReader;
import com.mmnaseri.couteau.strings.document.SnippetParser;
import com.mmnaseri.couteau.strings.token.impl.ContainedTokenReader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/22 AD, 9:53)
 */
public class ListParser implements SnippetParser<List<String>> {

    private final Pattern opening;
    private final Pattern closing;
    private final Pattern container;
    private final Pattern delimiter;

    public ListParser() {
        this((String) null);
    }

    public ListParser(Pattern container) {
        this(null, container);
    }

    public ListParser(Pattern closing, Pattern container) {
        this(null, closing, container);
    }

    public ListParser(Pattern opening, Pattern closing, Pattern container) {
        this.opening = opening;
        this.closing = closing != null ? closing : Pattern.compile("$");
        this.container = container != null ? container : Pattern.compile("['\"`]");
        delimiter = Pattern.compile("(" + this.container.toString() + "|" + this.closing.toString() + "|$|,)");
    }

    public ListParser(String opening) {
        this(opening, null);
    }

    public ListParser(String opening, String closing) {
        this(opening, closing, null);
    }

    public ListParser(String opening, String closing, String container) {
        this(opening != null ? Pattern.compile(opening) : null, closing != null ? Pattern.compile(closing) : null, container != null ? Pattern.compile(container) : null);
    }


    @Override
    public List<String> parse(DocumentReader reader) {
        reader.remember();
        final ArrayList<String> list = new ArrayList<String>();
        if (opening != null) {
            reader.expect(opening, false);
        }
        while (reader.hasMore() && !reader.has(closing)) {
            final String value = reader.read(reader.nextToken(new ContainedTokenReader(container.toString(), container.toString(), '\\', delimiter)));
            reader.skip(Pattern.compile("\\s*,\\s*"));
            list.add(value);
        }
        reader.skip(closing);
        reader.forget();
        return list;
    }

}
