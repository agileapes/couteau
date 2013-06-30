package com.agileapes.couteau.context.contract;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/29, 13:38)
 */
public interface ContextAware<E> {

    void setContext(Context<E> context);

}
