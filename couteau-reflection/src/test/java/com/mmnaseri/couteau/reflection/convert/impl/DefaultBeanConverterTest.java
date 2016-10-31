/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
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

package com.mmnaseri.couteau.reflection.convert.impl;

import com.mmnaseri.couteau.reflection.test.Author;
import com.mmnaseri.couteau.reflection.test.Book;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
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
