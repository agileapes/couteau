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

package com.agileapes.couteau.strings.document.impl;

import com.agileapes.couteau.strings.token.Token;
import com.agileapes.couteau.strings.token.impl.PatternTokenReader;
import com.agileapes.couteau.strings.token.impl.TaggedTokenReader;
import com.agileapes.couteau.strings.token.impl.UnsignedDecimalTokenReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/23/13, 1:11 PM)
 */
public class DefaultDocumentReaderTest {

    @Test
    public void testReadingSimpleExpression() throws Exception {
        int number = 1;
        int sign = 2;
        final DefaultDocumentReader reader = new DefaultDocumentReader("5 + 12 - 3.1");
        reader.addReader(new TaggedTokenReader(number, new UnsignedDecimalTokenReader()));
        reader.addReader(new TaggedTokenReader(sign, new PatternTokenReader("^[\\+\\-]")));
        List<String> values = new ArrayList<String>();
        List<Integer> tokens = new ArrayList<Integer>();
        while (reader.hasMore()) {
            reader.skip(Pattern.compile("\\s+"));
            if (!reader.hasMore()) {
                break;
            }
            final Set<Token> readTokens = reader.nextToken();
            if (readTokens.size() != 1) {
                throw new Exception();
            }
            final String read = reader.read(readTokens.iterator().next());
            values.add(read);
            tokens.add(readTokens.iterator().next().getTag());
        }
        Assert.assertEquals(values.size(), tokens.size());
        Assert.assertEquals(values.size(), 5);
        Assert.assertEquals(values.get(0), "5");
        Assert.assertEquals(values.get(1), "+");
        Assert.assertEquals(values.get(2), "12");
        Assert.assertEquals(values.get(3), "-");
        Assert.assertEquals(values.get(4), "3.1");
        Assert.assertEquals(tokens.get(0), (Integer) number);
        Assert.assertEquals(tokens.get(1), (Integer) sign);
        Assert.assertEquals(tokens.get(2), (Integer) number);
        Assert.assertEquals(tokens.get(3), (Integer) sign);
        Assert.assertEquals(tokens.get(4), (Integer) number);
    }
}
