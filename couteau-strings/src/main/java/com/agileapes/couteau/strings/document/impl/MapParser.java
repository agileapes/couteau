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

package com.agileapes.couteau.strings.document.impl;

import com.agileapes.couteau.strings.document.DocumentReader;
import com.agileapes.couteau.strings.document.SnippetParser;
import com.agileapes.couteau.strings.token.impl.ContainedTokenReader;
import com.agileapes.couteau.strings.token.impl.DelimitedTokenReader;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/7/30, 4:39)
 */
public class MapParser implements SnippetParser<Map<String, String>> {

    public static enum Container {

        CURLY('}', "\\{", "\\}"),
        ROUNDED(')', "\\(", "\\)"),
        ANGLED('>', "<", ">"),
        SQUARE(']', "\\[", "\\]");

        private final Character closingCharacter;
        private final String openingPattern;
        private final String closingPattern;

        private Container(Character closingCharacter, String openingPattern, String closingPattern) {
            this.closingCharacter = closingCharacter;
            this.openingPattern = openingPattern;
            this.closingPattern = closingPattern;
        }

        public Character getClosingCharacter() {
            return closingCharacter;
        }

        public String getOpeningPattern() {
            return openingPattern;
        }

        public String getClosingPattern() {
            return closingPattern;
        }
    }

    private final Container container;

    public MapParser(Container container) {
        this.container = container;
    }

    @Override
    public Map<String, String> parse(DocumentReader reader) {
        final Map<String, String> map = new HashMap<String, String>();
        reader.expect(container.getOpeningPattern(), false);
        boolean broken = false;
        while (reader.hasMore()) {
            String value = null;
            //we read the name by going on until we reach a point where a delimiter is met
            String name = reader.read(reader.expectToken(new DelimitedTokenReader(container.getClosingCharacter().toString(), ",", "=")));
            //then we check for the delimiter to see what we must do next
            String delimiter = reader.expect("[" + container.getClosingPattern() + ",=]", false);
            if (delimiter.equals("=")) {
                //if we have reached a '=', we must now read the value
                value = reader.read(reader.expectToken(new ContainedTokenReader("\"'`", Pattern.compile("[," + container.getClosingPattern() + "]"))));
                delimiter = reader.expect("[" + container.getClosingPattern() + ",]", false);
            }
            if (!name.isEmpty()) {
                map.put(name, value);
            }
            if (delimiter.equals(container.getClosingCharacter().toString())) {
                broken = true;
                break;
            }
        }
        if (!broken) {
            throw new IllegalStateException();
        }
        return map;
    }

}
