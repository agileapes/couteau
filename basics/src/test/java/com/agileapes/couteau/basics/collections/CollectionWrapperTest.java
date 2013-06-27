package com.agileapes.couteau.basics.collections;

import com.agileapes.couteau.basics.api.Filter;
import com.agileapes.couteau.basics.api.Mapper;
import com.agileapes.couteau.basics.api.Processor;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.agileapes.couteau.basics.collections.CollectionWrapper.with;

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
            public void process(Integer input) throws Exception {
                value.set(value.get() + input);
            }
        });
        Assert.assertEquals(value.get(), 10);
    }

    @Test
    public void testFiltering() throws Exception {
        final List<Integer> list = with(1, 2, 3, 4, 5, 6).filter(new Filter<Integer>() {
            @Override
            public boolean accepts(Integer item) throws Exception {
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
        final List<Integer> list = with(1, 2, 3, 4).map(new Mapper<Integer, Integer>() {
            @Override
            public Integer map(Integer input) throws Exception {
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
        final List<Collection<Integer>> list = with(1, 2, 3, 4, 5, 6).expand(new Mapper<Collection<Integer>, Collection<Collection<Integer>>>() {
            @Override
            public Collection<Collection<Integer>> map(Collection<Integer> input) throws Exception {
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
                .all(new Mapper<Collection<List<Integer>>, Collection<List<Integer>>>() {
                    @Override
                    public Collection<List<Integer>> map(Collection<List<Integer>> input) throws Exception {
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
                .map(new Mapper<List<Integer>, Integer>() {
                    @Override
                    public Integer map(List<Integer> input) throws Exception {
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
