package com.agileapes.couteau.basics.api;

/**
 * Filters are designed to pick out some items from a collection, dividing the collection into two partitions.
 * The result of the filtering should be deterministic, i.e., the accept method's return value should not defer
 * for the same input item.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 2:54 PM)
 */
public interface Filter<E> {

    /**
     * Will determine whether or not an item is accepted by the filter
     * @param item    the item
     * @return {@code true} if the item is accepted
     * @throws Exception
     */
    boolean accepts(E item) throws Exception;

}
