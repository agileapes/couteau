package com.agileapes.couteau.reflection.test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 3:05 PM)
 */
public class PublishedBook extends Book {

    private final int yearOfPublication;
    private final String publisher;
    private final String isbn;

    public PublishedBook(Book book, int yearOfPublication, String publisher, String isbn) {
        this.setAuthor(book.getAuthor());
        this.setTitle(book.getTitle());
        this.setYearOfWriting(book.getYearOfWriting());
        this.yearOfPublication = yearOfPublication;
        this.publisher = publisher;
        this.isbn = isbn;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn() {
        return isbn;
    }

}
