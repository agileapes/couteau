package com.agileapes.couteau.basics.collections;

import com.agileapes.couteau.basics.api.Filter;
import com.agileapes.couteau.basics.api.Mapper;
import com.agileapes.couteau.basics.api.Processor;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 2:57 PM)
 */
public class CollectionWrapper<I> {

    public static <I> CollectionWrapper<I> with(I... items) {
        return with(Arrays.asList(items));
    }
    
    public static <I> CollectionWrapper<I> with(Collection<I> items) {
        return new CollectionWrapper<I>(items);
    }
    
    private final List<I> items;

    private CollectionWrapper(Collection<I> items) {
        this.items = new ArrayList<I>(items);
    }

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

    public CollectionWrapper<I> sort(Comparator<I> comparator) {
        final ArrayList<I> sorted = new ArrayList<I>(items);
        Collections.sort(sorted, comparator);
        return new CollectionWrapper<I>(sorted);
    }

    public CollectionWrapper<I> each(Processor<I> processor) throws Exception {
        for (I item : items) {
            processor.process(item);
        }
        return this;
    }

    public CollectionWrapper<I> filter(Filter<I> filter) throws Exception {
        final ArrayList<I> filtered = new ArrayList<I>();
        for (I item : items) {
            if (filter.accepts(item)) {
                filtered.add(item);
            }
        }
        return new CollectionWrapper<I>(filtered);
    }

    public <O> CollectionWrapper<O> map(Mapper<I, O> mapper) throws Exception {
        final ArrayList<O> mapped = new ArrayList<O>();
        for (I item : items) {
            mapped.add(mapper.map(item));
        }
        return new CollectionWrapper<O>(mapped);
    }

    public CollectionWrapper<Collection<I>> expand(Mapper<Collection<I>, Collection<Collection<I>>> expander) throws Exception {
        return new CollectionWrapper<Collection<I>>(expander.map(new ArrayList<I>(items)));
    }

    public CollectionWrapper<I> all(Mapper<Collection<I>, Collection<I>> mapper) throws Exception {
        return new CollectionWrapper<I>(mapper.map(new ArrayList<I>(items)));
    }

    public List<I> concurrentList() {
        return new CopyOnWriteArrayList<I>(items);
    }

    public List<I> list() {
        return new ArrayList<I>(items);
    }

    @SuppressWarnings("unchecked")
    public I[] array() {
        return (I[]) items.toArray();
    }
    
    public int count() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public I first() {
        if (isEmpty()) {
            return null;
        }
        return items.get(0);
    }

    public I last() {
        if (isEmpty()) {
            return null;
        }
        return items.get(items.size() - 1);
    }

}
