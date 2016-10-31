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

package com.agileapes.couteau.basics.collections;

import com.mmnaseri.couteau.basics.api.Filter;
import com.mmnaseri.couteau.basics.api.Processor;
import com.mmnaseri.couteau.basics.api.Transformer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mmnaseri.couteau.basics.collections.CollectionWrapper.with;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 3:16 PM)
 */
public class CollectionWrapperTest {

    @Test
    public void testSorting() throws Exception {
        Assert.assertEquals(with(9, 8, 7, 6).sort().list(), Arrays.asList(6, 7, 8, 9));
    }

    @Test
    public void testProcessing() throws Exception {
        final AtomicInteger value = new AtomicInteger(0);
        with(1, 2, 3, 4).each(new Processor<Integer>() {
            @Override
            public void process(Integer input) {
                value.set(value.get() + input);
            }
        });
        Assert.assertEquals(value.get(), 10);
    }

    @Test
    public void testFiltering() throws Exception {
        //noinspection unchecked
        final List<Integer> list = with(1, 2, 3, 4, 5, 6).keep(new Filter<Integer>() {
            @Override
            public boolean accepts(Integer item) {
                return item % 2 == 0;
            }
        }).list();
        Assert.assertEquals(list.size(), 3);
        Assert.assertTrue(list.contains(2));
        Assert.assertTrue(list.contains(4));
        Assert.assertTrue(list.contains(6));
    }

    @Test
    public void testMapping() throws Exception {
        final List<Integer> list = with(1, 2, 3, 4).transform(new Transformer<Integer, Integer>() {
            @Override
            public Integer map(Integer input) {
                return input * 2;
            }
        }).list();
        Assert.assertEquals(list.size(), 4);
        Assert.assertTrue(list.contains(2));
        Assert.assertTrue(list.contains(4));
        Assert.assertTrue(list.contains(6));
        Assert.assertTrue(list.contains(8));
    }

    @Test
    public void testExpansion() throws Exception {
        final List<Collection<Integer>> list = with(1, 2, 3, 4, 5, 6).expand(new Transformer<Collection<Integer>, Collection<Collection<Integer>>>() {
            @Override
            public Collection<Collection<Integer>> map(Collection<Integer> input) {
                final ArrayList<Collection<Integer>> collections = new ArrayList<Collection<Integer>>();
                final HashSet<Integer> odds = new HashSet<Integer>();
                final HashSet<Integer> evens = new HashSet<Integer>();
                collections.add(odds);
                collections.add(evens);
                for (Integer integer : input) {
                    if (integer % 2 == 0) {
                        evens.add(integer);
                    } else {
                        odds.add(integer);
                    }
                }
                return collections;
            }
        }).list();
        Assert.assertEquals(list.size(), 2);
        Collection<Integer> evens = list.get(0).iterator().next() % 2 == 0 ? list.get(0) : list.get(1);
        Collection<Integer> odds = list.get(1).iterator().next() % 2 == 1 ? list.get(1) : list.get(0);
        Assert.assertEquals(evens.size(), 3);
        Assert.assertEquals(odds.size(), 3);
        Assert.assertTrue(evens.contains(2));
        Assert.assertTrue(evens.contains(4));
        Assert.assertTrue(evens.contains(6));
        Assert.assertTrue(odds.contains(1));
        Assert.assertTrue(odds.contains(3));
        Assert.assertTrue(odds.contains(5));
    }

    @Test
    public void testReduction() throws Exception {
        //noinspection unchecked
        final List<Integer> list = with(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6), Arrays.asList(7, 8, 9))
                .all(new Transformer<Collection<List<Integer>>, Collection<List<Integer>>>() {
                    @Override
                    public Collection<List<Integer>> map(Collection<List<Integer>> input) {
                        final ArrayList<List<Integer>> lists = new ArrayList<List<Integer>>();
                        for (List<Integer> list : input) {
                            int max = 0;
                            for (Integer integer : list) {
                                max = Math.max(max, integer);
                            }
                            lists.add(Arrays.asList(max));
                        }
                        return lists;
                    }
                })
                .transform(new Transformer<List<Integer>, Integer>() {
                    @Override
                    public Integer map(List<Integer> input) {
                        return input.iterator().next();
                    }
                })
                .list();
        Assert.assertEquals(list.size(), 3);
        Assert.assertTrue(list.contains(3));
        Assert.assertTrue(list.contains(6));
        Assert.assertTrue(list.contains(9));
    }
}
