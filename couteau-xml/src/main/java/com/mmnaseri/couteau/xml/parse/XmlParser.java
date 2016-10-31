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

package com.mmnaseri.couteau.xml.parse;

import com.mmnaseri.couteau.xml.error.XmlParseError;
import com.mmnaseri.couteau.xml.node.XmlNode;

import java.io.InputStream;

/**
 * The XmlParser interface allows for easily parsing XML documents from an input stream into an tree representation.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/7/29, 19:43)
 */
public interface XmlParser {

    /**
     * <p>This method will take in an input stream and reading the XML content it points to, will return a
     * tree representation of the given XML document.</p>
     *
     * <p>Because the tree representation adds much metadata to the XML document, it might be unwise to use
     * this facility for XML documents that are considered <em>too large</em>. Just how large this means, is
     * up to resources and careful benchmarking of the tool on the target machine.</p>
     * @param source    the input steam which will present the XML document
     * @return a parsed, tree representation of the tree
     * @throws XmlParseError
     */
    XmlNode parse(InputStream source) throws XmlParseError;

}
