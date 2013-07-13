package com.agileapes.couteau.reflection.beans.impl;

import com.agileapes.couteau.reflection.test.Book;
import com.agileapes.couteau.reflection.test.PublishedBook;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import com.agileapes.couteau.reflection.util.assets.MemberNameFilter;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
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
        book.setAuthor("Myself");
        book.setYearOfWriting(1990);
        final MethodInvocation invocation = new MethodInvocation(ReflectionUtils.withMethods(Book.class).first(), book, 2000, "My Publisher");
        final Object result = invocation.invoke();
        Assert.assertTrue(result instanceof PublishedBook);
        final PublishedBook publishedBook = (PublishedBook) result;
        Assert.assertEquals(publishedBook.getAuthor(), book.getAuthor());
        Assert.assertEquals(publishedBook.getTitle(), book.getTitle());
        Assert.assertEquals(publishedBook.getYearOfWriting(), book.getYearOfWriting());
        Assert.assertEquals(publishedBook.getPublisher(), "My Publisher");
        Assert.assertEquals(publishedBook.getYearOfPublication(), 2000);
        Assert.assertNotNull(publishedBook.getIsbn());
    }
}
