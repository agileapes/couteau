package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.reflection.test.Book;
import com.agileapes.couteau.reflection.test.PublishedBook;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 3:13 PM)
 */
public class ConstructorBeanInitializerTest {

    @Test
    public void testBeanInitialization() throws Exception {
        final ConstructorBeanInitializer initializer = new ConstructorBeanInitializer();
        final Book book = initializer.initialize(Book.class, new Class[0], new Object[0]);
        Assert.assertNotNull(book);
    }

    @Test
    public void testBeanInstantiationWithConstructorArgs() throws Exception {
        final ConstructorBeanInitializer initializer = new ConstructorBeanInitializer();
        final PublishedBook publishedBook = initializer.initialize(PublishedBook.class, new Class[]{Book.class, int.class, String.class, String.class}, new Object[]{new Book(), 2000, "Some Publisher", "123456"});
        Assert.assertNotNull(publishedBook);
    }

}
