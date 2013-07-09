package com.agileapes.couteau.basics.api;

/**
 * A transformer is a functional piece of work that will deterministically transform an input item, into another item --
 * which is not necessarily of the same type.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 2:55 PM)
 */
public interface Transformer<I, O> {

    /**
     * Will transform the input item to the output
     * @param input    the input
     * @return the output
     * @throws Exception
     */
    O map(I input) throws Exception;

}
