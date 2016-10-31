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

package com.agileapes.couteau.graph.binding.impl;

import com.agileapes.couteau.graph.binding.model.Simple;
import com.agileapes.couteau.graph.tree.node.impl.BinaryTreeNode;
import com.mmnaseri.couteau.context.value.impl.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/3/7 AD, 19:26)
 */
public class DefaultObjectReaderTest {

    @SuppressWarnings("unchecked")
    private BinaryTreeNode getTree() {
        final BinaryTreeNode root = new BinaryTreeNode();
        final BinaryTreeNode leftChild = new BinaryTreeNode();
        leftChild.setAttribute("name", "NodeName");
        root.setLeftChild(leftChild);
        final BinaryTreeNode rightChild = new BinaryTreeNode();
        rightChild.setAttribute("id", "1234");
        final BinaryTreeNode grandChild = new BinaryTreeNode();
        grandChild.setAttribute("date", "2012/12/21");
        rightChild.setLeftChild(grandChild);
        root.setRightChild(rightChild);
        return root;
    }

    @Test
    public void testSimpleBeanReading() throws Exception {
        final DefaultValueReaderContext valueReaderContext = new DefaultValueReaderContext();
        valueReaderContext.register(new ClassValueReader());
        valueReaderContext.register(new DateValueReader());
        valueReaderContext.register(new EnumValueReader());
        valueReaderContext.register(new FileValueReader());
        valueReaderContext.register(new PrimitiveValueReader());
        valueReaderContext.register(new UrlValueReader());
        final DefaultObjectReader<BinaryTreeNode> objectReader = new DefaultObjectReader<BinaryTreeNode>(valueReaderContext);
        final BinaryTreeNode root = getTree();
        final Simple readObject = objectReader.read(Simple.class, root);
        Assert.assertNotNull(readObject);
        Assert.assertEquals(readObject.getName(), "NodeName");
        Assert.assertEquals(readObject.getDate(), new GregorianCalendar(2012, Calendar.DECEMBER, 21).getTime());
        Assert.assertEquals((long) readObject.getId(), 1234L);
    }

}
