package com.agileapes.couteau.strings.token.impl;

import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/23/13, 1:19 PM)
 */
public class UnsignedIntegerTokenReader extends PatternTokenReader {

    public static final Pattern PATTERN = Pattern.compile("\\d+");

    public UnsignedIntegerTokenReader() {
        super(PATTERN);
    }

}
