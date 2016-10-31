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

package com.mmnaseri.couteau.strings.replace;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helps with replacing the text in a given string using a callback, allowing for dynamic modification of
 * matched values.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/14/13, 4:59 PM)
 */
public class CallbackMatcher {

    private final Pattern pattern;
    private final String text;

    public CallbackMatcher(String pattern, String text) {
        this(Pattern.compile(pattern), text);
    }

    public CallbackMatcher(Pattern pattern, String text) {
        this.pattern = pattern;
        this.text = text;
    }

    /**
     * Replaces the text wrapped in the object with the given callback's return value
     * @param callback    the callback that will determine what the replacement should be
     * @return the modified text
     */
    public String replace(ReplaceCallback callback) {
        final Matcher matcher = pattern.matcher(text);
        final List<MatchToken> tokens = new ArrayList<MatchToken>();
        int pos = 0;
        while (matcher.find()) {
            if (pos < matcher.start()) {
                tokens.add(new MatchToken(text.substring(pos, matcher.start())));
            }
            tokens.add(new MatchToken(matcher.toMatchResult()));
            pos = matcher.end();
        }
        if (pos < text.length()) {
            tokens.add(new MatchToken(text.substring(pos)));
        }
        final StringBuilder builder = new StringBuilder();
        for (final MatchToken token : tokens) {
            final String value;
            if (token.getMatchResult() != null) {
                value = callback.replace(token.getMatchResult());
            } else {
                value = token.getValue();
            }
            builder.append(value);
        }
        return builder.toString();
    }

}
