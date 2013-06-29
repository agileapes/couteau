package com.agileapes.couteau.basics.api;

/**
 * the processor will perform a single unit of work on an item. This work is usually uniformly spanned across a whole
 * collection of items.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 2:55 PM)
 */
public interface Processor<E> {

    /**
     * The processor's implementation of the work to be done
     * @param input    the input item
     * @throws Exception
     */
    void process(E input) throws Exception;

}
