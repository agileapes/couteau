package com.agileapes.couteau.reflection.convert.impl;

import com.agileapes.couteau.reflection.test.Author;
import com.agileapes.couteau.reflection.test.Book;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 9:16 AM)
 */
public class DefaultBeanConverterTest {

    private Author author(String name, int age) {
        final Author author = new Author();
        author.setName(name);
        author.setAge(age);
        return author;
    }

    @Test
    public void testConversionOfSameType() throws Exception {
        final Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthors(Arrays.asList(author("First", 5), author("second", 10)));
        book.setYearOfWriting(2000);
        DefaultBeanConverter converter = new DefaultBeanConverter();
        final Book converted = converter.convert(book, Book.class);
        Assert.assertNotNull(converted);
        Assert.assertEquals(converted.getAuthors(), book.getAuthors());
        Assert.assertEquals(converted.getTitle(), book.getTitle());
        Assert.assertEquals(converted.getYearOfWriting(), book.getYearOfWriting());
    }

}
