package com.agileapes.couteau.strings.token.impl;

import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/23/13, 1:14 PM)
 */
public class UnsignedDecimalTokenReader extends PatternTokenReader {

    private static final Pattern PATTERN = Pattern.compile("^\\d+(?:\\.\\d+)?");

    public UnsignedDecimalTokenReader() {
        super(PATTERN);
    }

}
