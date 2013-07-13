package com.agileapes.couteau.reflection.test;

import java.util.UUID;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 3:04 PM)
 */
public class Book {

    private String title;
    private String author;
    private int yearOfWriting;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfWriting() {
        return yearOfWriting;
    }

    public void setYearOfWriting(int yearOfWriting) {
        this.yearOfWriting = yearOfWriting;
    }

    public PublishedBook publish(int year, String publisher) {
        final String isbn = UUID.randomUUID().toString();
        return new PublishedBook(this, year, publisher, isbn);
    }

}
