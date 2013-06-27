package com.agileapes.couteau.context.contract;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:12 PM)
 */
public interface ContextProcessor<E> {

    void postProcessContext(Context<E> context);

}
