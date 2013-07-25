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
     * Will determine whether or not an item is accepted by the keep
     * @param item    the item
     * @return {@code true} if the item is accepted
     */
    boolean accepts(E item);

}
