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
