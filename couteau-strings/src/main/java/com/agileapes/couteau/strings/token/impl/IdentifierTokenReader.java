package com.agileapes.couteau.strings.token.impl;

import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/23/13, 1:22 PM)
 */
public class IdentifierTokenReader extends PatternTokenReader {

    public static final Pattern PATTERN = Pattern.compile("[a-z_][a-z_\\d]*", Pattern.CASE_INSENSITIVE);

    public IdentifierTokenReader() {
        super(PATTERN);
    }

}
