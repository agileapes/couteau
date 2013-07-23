package com.agileapes.couteau.strings.document;

/**
 * The snippet parser will allow for specific parsing rules to be put together in a reusable fashion
 * as a single unit of work
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/23/13, 11:25 AM)
 */
public interface SnippetParser {

    /**
     * This method will be called by an instance of {@link DocumentReader} through the
     * {@link DocumentReader#parse(SnippetParser)} method
     * @param reader the reader which holds the current document
     */
    void parse(DocumentReader reader);

}
