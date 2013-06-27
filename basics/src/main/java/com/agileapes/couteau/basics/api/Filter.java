package com.agileapes.couteau.basics.api;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 2:54 PM)
 */
public interface Filter<E> {

    boolean accepts(E item) throws Exception;

}
