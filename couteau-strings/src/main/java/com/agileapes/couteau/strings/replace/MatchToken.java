package com.agileapes.couteau.strings.replace;

import java.util.regex.MatchResult;

/**
 * This class denotes the token that was resulted from a matching process
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/14/13, 5:18 PM)
 */
class MatchToken {

    private MatchResult matchResult;
    private String value;

    MatchToken(String value) {
        this(null, value);
    }

    MatchToken(MatchResult matchResult) {
        this(matchResult, null);
    }

    private MatchToken(MatchResult matchResult, String value) {
        this.matchResult = matchResult;
        this.value = value;
    }

    MatchResult getMatchResult() {
        return matchResult;
    }

    String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return (matchResult != null ? "[" : "(") + value + (matchResult != null ? "]" : ")");
    }

}
