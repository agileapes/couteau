package com.agileapes.couteau.basics.collections;

import com.agileapes.couteau.basics.api.Filter;
import com.agileapes.couteau.basics.api.Mapper;
import com.agileapes.couteau.basics.api.Processor;

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
     * @param filter    the keep which determines which items will pass through
     * @return the wrapper for all accepted items
     * @throws Exception
     */
    public CollectionWrapper<I> keep(Filter<? super I> filter) throws Exception {
        final ArrayList<I> filtered = new ArrayList<I>();
        for (I item : items) {
            if (filter.accepts(item)) {
                filtered.add(item);
            }
        }
        return new CollectionWrapper<I>(filtered);
    }

    /**
     * Will return a <em>new</em> wrapper for all items not acceptable by the keep
     * @param filter    the keep which determines which items will pass through
     * @return the wrapper for all accepted items
     * @throws Exception
     */
    public CollectionWrapper<I> drop(Filter<? super I> filter) throws Exception {
        final ArrayList<I> filtered = new ArrayList<I>();
        for (I item : items) {
            if (!filter.accepts(item)) {
                filtered.add(item);
            }
        }
        return new CollectionWrapper<I>(filtered);
    }

    /**
     * Will return a wrapper for a collection of items that are produced through the mapping of the currently wrapped items
     * @param mapper    the mapper
     * @param <O>       the type of output objects
     * @return the <em>newly instantiated</em> wrapper for the mapper's output
     * @throws Exception
     */
    public <O> CollectionWrapper<O> map(Mapper<? super I, O> mapper) throws Exception {
        final ArrayList<O> mapped = new ArrayList<O>();
        for (I item : items) {
            mapped.add(mapper.map(item));
        }
        return new CollectionWrapper<O>(mapped);
    }

    /**
     * Will produce a collection of collections for items in this wrapper. This is helpful when partitioning
     * items in the wrapper
     * @param expander    the expander
     * @return the wrapper
     * @throws Exception
     */
    public CollectionWrapper<Collection<I>> expand(Mapper<Collection<I>, Collection<Collection<I>>> expander) throws Exception {
        return new CollectionWrapper<Collection<I>>(expander.map(new ArrayList<I>(items)));
    }

    /**
     * Will map all the items in the collection into instances of the wrapped items. This is useful when reducing
     * certain items in the collection
     * @param mapper    the mapper
     * @return the wrapper
     * @throws Exception
     */
    public CollectionWrapper<I> all(Mapper<Collection<I>, Collection<I>> mapper) throws Exception {
        return new CollectionWrapper<I>(mapper.map(new ArrayList<I>(items)));
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

}
