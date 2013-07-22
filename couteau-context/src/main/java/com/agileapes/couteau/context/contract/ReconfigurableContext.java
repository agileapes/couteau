package com.agileapes.couteau.context.contract;

/**
 * A reconfigurable context is a context that can be reconfigured and then refreshed. The semantics of this context
 * requires that you refresh it after reconfiguring.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:25 PM)
 */
public interface ReconfigurableContext<E> extends Context<E> {

    /**
     * This method will cause the context to be refreshed. Note that this means that the startup
     * process for the context will be repeated. This has the side-effect that all {@link ContextProcessor}s
     * will run again over the context.
     */
    void refresh();

}
