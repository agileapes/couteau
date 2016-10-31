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

import com.mmnaseri.couteau.context.contract.Context;
import com.mmnaseri.couteau.context.error.ValueReaderError;

import java.lang.reflect.Type;

/**
 * The value reader context is the one value reader to rule them all. It will aggregate
 * all value readers into a single value reader that has the capability of all of them
 * in one place.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/30, 14:06)
 */
public interface ValueReaderContext extends ValueReader, Context<ValueReader> {

    /**
     * Reads a value based on the given type. This enables support for reading generically-bound values
     * @param text    the text to read
     * @param type    the type argument to match to
     * @return the matched value
     * @throws ValueReaderError
     */
    Object read(String text, Type type) throws ValueReaderError;

}
