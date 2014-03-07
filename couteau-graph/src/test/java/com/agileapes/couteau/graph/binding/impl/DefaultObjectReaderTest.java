package com.agileapes.couteau.graph.binding.impl;

import com.agileapes.couteau.context.value.impl.*;
import com.agileapes.couteau.graph.binding.model.Simple;
import com.agileapes.couteau.graph.query.NodePattern;
import com.agileapes.couteau.graph.tree.node.impl.BinaryTreeNode;
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
