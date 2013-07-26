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
 * This token reader will read values that are contained within a pair of designator
 * characters (for instance, those contained within a pair of quotation marks).
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/26, 11:23)
 */
public class ContainedTokenReader implements TokenReader {

    /**
     * The default escape character, '\'
     */
    public static final Character DEFAULT_ESCAPE_CHARACTER = '\\';

    /**
     * The string containing all opening characters
     */
    private final String opening;

    /**
     * The string containing all closing characters
     */
    private final String closing;

    /**
     * The escape character used for escaping closing character instances that are
     * really meant as part of the token
     */
    private final Character escape;

    /**
     * Flag determining whether non-contained tokens can be constituted as valid
     * target tokens.
     */
    private final boolean allowNonContained;

    /**
     * Instantiates the reader by setting the closing characters the same as the opening
     * characters, by using the default escape character, and by assuming that non-contained
     * tokens are allowed.
     * @param opening              the string of opening characters
     * @see #ContainedTokenReader(String, String, Character, boolean)
     */
    public ContainedTokenReader(String opening) {
        this(opening, true);
    }

    /**
     * Instantiates the reader by setting the closing characters the same as the opening
     * characters, and by using the default escape character.
     * @param opening              the string of opening characters
     * @param allowNonContained    the flag determining whether non-contained tokens
     *                             can be read as a valid token from the input text.
     * @see #ContainedTokenReader(String, String, Character, boolean)
     */
    public ContainedTokenReader(String opening, boolean allowNonContained) {
        this(opening, opening, allowNonContained);
    }

    /**
     * Instantiates the reader by setting the {@link #allowNonContained} flag to
     * true, using the default escape character.
     * @param opening              the string of opening characters
     * @param closing              the string of closing characters
     * @see #ContainedTokenReader(String, String, Character, boolean)
     */
    public ContainedTokenReader(String opening, String closing) {
        this(opening, closing, true);
    }

    /**
     * Instantiates the reader by setting the escape character to {@link #DEFAULT_ESCAPE_CHARACTER}
     * @param opening              the string of opening characters
     * @param closing              the string of closing characters
     * @param allowNonContained    the flag determining whether non-contained tokens
     *                             can be read as a valid token from the input text.
     * @see #ContainedTokenReader(String, String, Character, boolean)
     */
    public ContainedTokenReader(String opening, String closing, boolean allowNonContained) {
        this(opening, closing, DEFAULT_ESCAPE_CHARACTER, allowNonContained);
    }

    /**
     * Instantiates the reader, while setting the closing characters the same as the
     * opening characters, and setting the {@link #allowNonContained} flag to {@code true}.
     * @param opening              the string of opening characters
     * @param escape               the string of closing characters.
     * @see #ContainedTokenReader(String, String, Character, boolean)
     */
    public ContainedTokenReader(String opening, Character escape) {
        this(opening, escape, true);
    }

    /**
     * Instantiates the reader, while setting the closing characters the same as the
     * opening characters.
     * @param opening              the string of opening characters
     * @param escape               the string of closing characters.
     * @param allowNonContained    the flag determining whether non-contained tokens
     *                             can be read as a valid token from the input text.
     * @see #ContainedTokenReader(String, String, Character, boolean)
     */
    public ContainedTokenReader(String opening, Character escape, boolean allowNonContained) {
        this(opening, opening, escape, allowNonContained);
    }

    /**
     * This instantiates the token reader, while presetting its behavioral parameters
     * @param opening              a string containing all opening characters (e.g.,
     *                             {@code "("}). The input text is expected to start
     *                             with a character in this string.
     * @param closing              a string containing all closing characters (e.e.,
     *                             {@code ")"}). The input text will be scanned for
     *                             the closing character from this string that corresponds
     *                             to the position of the opening character with which
     *                             the string was started. As such, the number of characters
     *                             within the opening and closing strings must match.
     * @param escape               the escape character allowing the input text to contain
     *                             the closing character without triggering the close (
     *                             e.g., {@code "(hello \) there)"} which results in
     *                             {@code "hello \) there"}, assuming that the escape
     *                             character is {@code "\"}). Setting the escape character
     *                             to {@code null} disables escaping.
     * @param allowNonContained    This flag determines whether or not an input text that
     *                             does not open with a valid opening character should be
     *                             considered valid or not. If this is set to {@code true},
     *                             the first non-whitespace token in the string starting
     *                             at the beginning of the text will be returned.
     * @throws NullPointerException if either the {@code opening} or {@code closing} parameters
     * are set to null.
     * @throws IllegalArgumentException if the number of characters in the opening and closing
     * strings do not match.
     * @throws IllegalStateException if the escape character has been set to a character that
     * can be found in either the closing or the opening string of characters.
     */
    public ContainedTokenReader(String opening, String closing, Character escape, boolean allowNonContained) {
        if (opening == null || closing == null) {
            throw new NullPointerException(String.format("Input values cannot be null: (%s,%s,%s)", opening, closing, escape));
        }
        if (opening.length() != closing.length()) {
            throw new IllegalArgumentException("Opening and closing values must match");
        }
        if (escape != null && (opening.contains(String.valueOf(escape)) || closing.contains(String.valueOf(escape)))) {
            throw new IllegalStateException("Escape character must not be included as a semantic specifier");
        }
        this.allowNonContained = allowNonContained;
        this.opening = opening;
        this.closing = closing;
        this.escape = escape;
    }

    /**
     * This method will attempt to read the token this reader recognizes from the text input.
     * If the input does not match the expectations, it is expected that a {@code null} value
     * be returned.
     * @param text    the text input
     * @return the read token or {@code null} if no valid tokens were found
     */
    @Override
    public Token read(String text) {
        //we first see if the input text is opened with a valid opening character
        if (!opening.contains(String.valueOf(text.charAt(0)))) {
            //if not, we check to see if non-contained tokens are allowed
            if (allowNonContained && text.matches("[^\\s]+.*")) {
                //if they are allowed and we can find one, we will return it.
                final Matcher matcher = Pattern.compile("^[^\\s]+").matcher(text);
                matcher.find();
                return new SimpleToken(0, matcher.group().length());
            }
            return null;
        }
        //we set the opening character
        final Character open = text.charAt(0);
        //and set the closing character to the corresponding character from the closing string
        final Character close = closing.charAt(opening.indexOf(open));
        //we start by skipping the opening character
        int position = 1;
        while (position < text.length()) {
            //if we find the closing character, and it has not been escaped, we can break out
            if (close.equals(text.charAt(position)) && (escape == null || !text.substring(1, position).endsWith(String.valueOf(escape)))) {
                break;
            }
            position ++;
        }
        //if we have overrun the text, or the character currently being read is not the
        //expected closing character, the input is not valid
        if (position >= text.length() || !close.equals(text.charAt(position))) {
            return null;
        }
        return new SimpleToken(1, position, 1, Token.NO_TAG);
    }

}
