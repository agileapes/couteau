package com.agileapes.couteau.strings.token;

/**
 * Token readers are abstract representations of the process of reading a single token from a given text.
 * It is assumed that each token reader will be capable of distinguishing a single type of token from the input text.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/23/13, 11:12 AM)
 */
public interface TokenReader {

    /**
     * This method will attempt to read the token this reader recognizes from the text input.
     * If the input does not match the expectations, it is expected that a {@code null} value
     * be returned.
     * @param text    the text input
     * @return the read token or {@code null} if no valid tokens were found
     */
    Token read(String text);

}
