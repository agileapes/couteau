package com.agileapes.couteau.reflection.convert.impl;

import com.agileapes.couteau.reflection.test.Book;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 9:16 AM)
 */
public class DefaultBeanConverterTest {

    @Test
    public void testConversionOfSameType() throws Exception {
        final Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Book Author");
        book.setYearOfWriting(2000);
        DefaultBeanConverter converter = new DefaultBeanConverter();
        final Book converted = converter.convert(book, Book.class);
        Assert.assertNotNull(converted);
        Assert.assertEquals(converted.getAuthor(), book.getAuthor());
        Assert.assertEquals(converted.getTitle(), book.getTitle());
        Assert.assertEquals(converted.getYearOfWriting(), book.getYearOfWriting());
    }

}
