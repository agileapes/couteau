package com.agileapes.couteau.reflection.test;

import java.util.List;
import java.util.UUID;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 3:04 PM)
 */
public class Book {

    private String title;
    private List<Author> authors;
    private int yearOfWriting;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
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
