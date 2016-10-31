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

package com.mmnaseri.couteau.context.value;

import com.mmnaseri.couteau.context.error.ValueReaderError;

/**
 * <p>The value reader is designed to help with the process of converting String values into
 * objects represented by those values. Each implementation must announce whether or not it
 * can read values from String for any given type.</p>
 *
 * <p>Each value can have multiple representations. For instance, a key-value pair might be written
 * as either {@code <key, value>} or {@code key=value}. We can have two value readers, one for each
 * representation, and register them with the context. This way, the context will know that for
 * reading a value of key-value type it has two candidates, and will feed each candidate with the
 * representation at hand and the right one will eventually come up and translate the text into its
 * object value.</p>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/30, 13:50)
 */
public interface ValueReader {

    /**
     * Determines whether or not this value reader is capable of translating the textual representation
     * of a given type into its actual object value.
     * @param type    the type to be queried
     * @return {@code true} if this value reader can read the type in question
     */
    boolean canRead(Class<?> type);

    /**
     * Tries to read the value as given by the textual representation into an instance of the
     * given type
     * @param text    textual representation of the data
     * @param type    the type of data
     * @param <E>     the type parameter for the object instance
     * @return the object instance for the value represented by the input text
     * @throws ValueReaderError
     */
    <E> E read(String text, Class<E> type) throws ValueReaderError;

}
