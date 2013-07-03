package com.agileapes.couteau.context.contract;

/**
 * This interface allows beans registered with a context to be aware of the context to which
 * they belong
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/29, 13:38)
 */
public interface ContextAware<E> {

    /**
     * Method used to inject context to the bean
     * @param context    the context with which this bean has been registered
     */
    void setContext(Context<E> context);

}
