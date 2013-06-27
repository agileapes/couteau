package com.agileapes.couteau.basics.api;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 2:55 PM)
 */
public interface Mapper<I, O> {

    O map(I input) throws Exception;

}
