package com.agileapes.couteau.context.contract;

/**
 * The context processor will be allowed access to the context to which it belongs with
 * a single call. The context processor might be called more than once, if the context is refreshed.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:12 PM)
 */
public interface ContextProcessor<E> {

    /**
     * This method will be called whenever the context has been readied
     * @param context    the context to be processed
     */
    void postProcessContext(Context<E> context);

}
