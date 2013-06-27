package com.agileapes.couteau.basics.api;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 2:55 PM)
 */
public interface Processor<E> {

    void process(E input) throws Exception;

}
