/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mmnaseri.couteau.reflection.beans.impl;

import com.mmnaseri.couteau.reflection.test.Author;
import com.mmnaseri.couteau.reflection.test.Book;
import com.mmnaseri.couteau.reflection.test.PublishedBook;
import com.mmnaseri.couteau.reflection.util.ReflectionUtils;
import com.mmnaseri.couteau.reflection.util.assets.MemberNameFilter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/9/13, 2:58 PM)
 */
public class MethodInvocationTest {

    @Test
    public void testHashCodeEquality() throws Exception {
        final MethodInvocationTest target = new MethodInvocationTest();
        final MethodInvocation first = new MethodInvocation(ReflectionUtils.withMethods(getClass()).keep(new MemberNameFilter(".*HashCode.*")).first(), target, 1, null, 3, "test");
        final MethodInvocation second = new MethodInvocation(ReflectionUtils.withMethods(getClass()).keep(new MemberNameFilter(".*HashCode.*")).first(), new MethodInvocationTest(), 1, null, 3, "test");
        final MethodInvocation third = new MethodInvocation(ReflectionUtils.withMethods(getClass()).keep(new MemberNameFilter(".*HashCode.*")).first(), target, 1, null, 3, "test");
        Assert.assertNotEquals(first, second);
        Assert.assertNotEquals(first.hashCode(), second.hashCode());
        Assert.assertEquals(first, third);
        Assert.assertEquals(first.hashCode(), third.hashCode());
    }

    @Test
    public void testMethodInvocation() throws Exception {
        final Book book = new Book();
        book.setTitle("My Book");
        book.setAuthors(Arrays.asList(new Author()));
        book.setYearOfWriting(1990);
        final MethodInvocation invocation = new MethodInvocation(ReflectionUtils.withMethods(Book.class).keep(new MemberNameFilter("publish")).first(), book, 2000, "My Publisher");
        final Object result = invocation.invoke();
        Assert.assertTrue(result instanceof PublishedBook);
        final PublishedBook publishedBook = (PublishedBook) result;
        Assert.assertEquals(publishedBook.getAuthors(), book.getAuthors());
        Assert.assertEquals(publishedBook.getTitle(), book.getTitle());
        Assert.assertEquals(publishedBook.getYearOfWriting(), book.getYearOfWriting());
        Assert.assertEquals(publishedBook.getPublisher(), "My Publisher");
        Assert.assertEquals(publishedBook.getYearOfPublication(), 2000);
        Assert.assertNotNull(publishedBook.getIsbn());
    }
}
