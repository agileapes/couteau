package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.reflection.test.Book;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 3:19 PM)
 */
public class FieldBeanAccessorTest {

    @Test
    public void testFields() throws Exception {
        final Book book = new Book();
        book.setTitle("My Book");
        book.setYearOfWriting(2000);
        final FieldBeanAccessor<Book> accessor = new FieldBeanAccessor<Book>(book);
        Assert.assertEquals(accessor.getPropertyNames().size(), 3);
        Assert.assertTrue(accessor.hasProperty("title"));
        Assert.assertTrue(accessor.hasProperty("author"));
        Assert.assertTrue(accessor.hasProperty("yearOfWriting"));
        Assert.assertEquals(accessor.getPropertyType("title"), String.class);
        Assert.assertEquals(accessor.getPropertyType("author"), String.class);
        Assert.assertEquals(accessor.getPropertyType("yearOfWriting"), int.class);
        Assert.assertEquals(accessor.getPropertyValue("title"), book.getTitle());
        //noinspection AssertEqualsBetweenInconvertibleTypesTestNG
        Assert.assertEquals(accessor.getPropertyValue("yearOfWriting"), book.getYearOfWriting());
    }
}
