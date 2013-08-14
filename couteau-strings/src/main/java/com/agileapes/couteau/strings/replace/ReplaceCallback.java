package com.agileapes.couteau.strings.replace;

import java.util.regex.MatchResult;

/**
 * This interface will allow for dynamically replacing any text matched using {@link CallbackMatcher} with a
 * value of choice
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/14/13, 5:18 PM)
 */
public interface ReplaceCallback {

    /**
     * This method will be called once for each match
     * @param matchResult    the match result that holds the data for each match
     * @return replaced value
     */
    String replace(MatchResult matchResult);

}
