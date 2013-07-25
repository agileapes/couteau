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

package com.agileapes.couteau.strings.document;

/**
 * This class represents a single snapshot of the readers status.
 * This will help with remembering the position of the cursor in the document.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/23/13, 12:45 PM)
 */
public class ReaderSnapshot {

    private final DocumentReader documentReader;
    private final int cursorPosition;
    private final int line;
    private final int column;
    private final long timestamp;

    public ReaderSnapshot(DocumentReader documentReader, int cursorPosition) {
        this.documentReader = documentReader;
        this.cursorPosition = cursorPosition;
        this.line = documentReader.getLine();
        this.column = documentReader.getColumn();
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * @return the document reader which generated this snapshot
     */
    public DocumentReader getDocumentReader() {
        return documentReader;
    }

    /**
     * @return the position of the cursor at the moment this snapshot was taken
     */
    public int getCursorPosition() {
        return cursorPosition;
    }

    /**
     * @return the timestamp of thesnapshot
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @return the line number
     */
    public int getLine() {
        return line;
    }

    /**
     * @return the column number
     */
    public int getColumn() {
        return column;
    }
}
