/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.couteau.basics.collections;

import com.agileapes.couteau.basics.api.Filter;
import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.basics.api.Transformer;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The collection wrapper class is designed to wrap a collection and perform fluently designated operations on it.
 * It will make chained operations on the collection so much easier.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 2:57 PM)
 */
public class CollectionWrapper<I> {

    /**
     * This method -- specially when statically imported -- will increase readability.
     * @param items    the array of items to be wrapped
     * @param <I>      the type of items in the collection
     * @return the wrapped to the collection
     */
    public static <I> CollectionWrapper<I> with(I... items) {
        return with(Arrays.asList(items));
    }

    /**
     * This method -- specially when statically imported -- will increase readability.
     * @param items    the collection to be wrapped
     * @param <I>      the type of items in the collection
     * @return the wrapped to the collection
     */
    public static <I> CollectionWrapper<I> with(Collection<I> items) {
        return new CollectionWrapper<I>(items);
    }
    
    private final List<I> items;

    private CollectionWrapper(Collection<I> items) {
        this.items = new ArrayList<I>(items);
    }

    /**
     * Will sort all "comparable" items in the wrapped collection
     * @return the wrapper for sorted items
     */
    public CollectionWrapper<I> sort() {
        return sort(new Comparator<I>() {
            @Override
            public int compare(I o1, I o2) {
                if (o1 instanceof Comparable && o2 instanceof Comparable) {
                    //noinspection unchecked
                    return ((Comparable) o1).compareTo(o2);
                }
                return 0;
            }
        });
    }

    /**
     * Sorts all the items in the wrapped collection based on the given comparator
     * @param comparator    the comparator for wrapped items
     * @return the wrapper for sorted items
     */
    public CollectionWrapper<I> sort(Comparator<? super I> comparator) {
        final ArrayList<I> sorted = new ArrayList<I>(items);
        Collections.sort(sorted, comparator);
        return new CollectionWrapper<I>(sorted);
    }

    /**
     * Will work the processor over each of the items in the collection
     * @param processor    the processor
     * @return the wrapper again
     * @throws Exception
     */
    public CollectionWrapper<I> each(Processor<? super I> processor) throws Exception {
        for (I item : items) {
            processor.process(item);
        }
        return this;
    }

    /**
     * Will return a <em>new</em> wrapper for all items acceptable by the keep
     * @param filters    The filters that decide whether or not this item will be kept.
     *                   If any one filter accepts the item, the item will be kept.
     * @return the wrapper for all accepted items
     * @throws Exception
     */
    public CollectionWrapper<I> keep(Filter<? super I>... filters) throws Exception {
        final ArrayList<I> filtered = new ArrayList<I>();
        for (I item : items) {
            boolean kept = false;
            for (Filter<? super I> filter : filters) {
                if (filter.accepts(item)) {
                    kept = true;
                    break;
                }
            }
            if (kept) {
                filtered.add(item);
            }
        }
        return new CollectionWrapper<I>(filtered);
    }

    /**
     * Will return a <em>new</em> wrapper for all items not acceptable by the keep
     * @param filters    the filters that decide whether or not the item will be dropped.
     *                   If any single filter accepts the item, it will drop.
     * @return the wrapper for all accepted items
     * @throws Exception
     */
    public CollectionWrapper<I> drop(Filter<? super I>... filters) throws Exception {
        final ArrayList<I> filtered = new ArrayList<I>();
        for (I item : items) {
            boolean dropped = false;
            for (Filter<? super I> filter : filters) {
                if (filter.accepts(item)) {
                    dropped = true;
                    break;
                }
            }
            if (!dropped) {
                filtered.add(item);
            }
        }
        return new CollectionWrapper<I>(filtered);
    }

    /**
     * Will return a wrapper for a collection of items that are produced through the mapping of the currently wrapped items
     * @param transformer    the transformer
     * @param <O>       the type of output objects
     * @return the <em>newly instantiated</em> wrapper for the transformer's output
     * @throws Exception
     */
    public <O> CollectionWrapper<O> transform(Transformer<? super I, O> transformer) throws Exception {
        return new CollectionWrapper<O>(map(transformer).values());
    }

    public <O> Map<I, O> map(Transformer<? super I, O> transformer) throws Exception {
        final HashMap<I, O> map = new HashMap<I, O>();
        for (I item : items) {
            map.put(item, transformer.map(item));
        }
        return map;
    }

    /**
     * Will produce a collection of collections for items in this wrapper. This is helpful when partitioning
     * items in the wrapper
     * @param expander    the expander
     * @return the wrapper
     * @throws Exception
     */
    public CollectionWrapper<Collection<I>> expand(Transformer<Collection<I>, Collection<Collection<I>>> expander) throws Exception {
        return new CollectionWrapper<Collection<I>>(expander.map(new ArrayList<I>(items)));
    }

    /**
     * Will transform all the items in the collection into instances of the wrapped items. This is useful when reducing
     * certain items in the collection
     * @param transformer    the transformer
     * @return the wrapper
     * @throws Exception
     */
    public CollectionWrapper<I> all(Transformer<Collection<I>, Collection<I>> transformer) throws Exception {
        return new CollectionWrapper<I>(transformer.map(new ArrayList<I>(items)));
    }

    /**
     * @return a thread-safe version of the items in the wrapper. Modifying this collection does not affect the wrapper.
     */
    public List<I> concurrentList() {
        return new CopyOnWriteArrayList<I>(items);
    }

    /**
     * @return a list of items in the wrapper. Modifying this collection does not affect the wrapper.
     */
    public List<I> list() {
        return new ArrayList<I>(items);
    }

    /**
     * @return an array of all the items in the wrapper
     */
    @SuppressWarnings("unchecked")
    public I[] array() {
        return (I[]) items.toArray();
    }

    /**
     * @return the number of items currently wrapped
     */
    public int count() {
        return items.size();
    }

    /**
     * @return {@code true} if this wrapper is currently empty
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * @return the first item in the wrapper or {@code null} if it is empty
     */
    public I first() {
        if (isEmpty()) {
            return null;
        }
        return items.get(0);
    }

    /**
     * @return the last item in the wrapper or {@code null} if it is empty
     */
    public I last() {
        if (isEmpty()) {
            return null;
        }
        return items.get(items.size() - 1);
    }

    public Set<I> set() {
        return new HashSet<I>(items);
    }
}
