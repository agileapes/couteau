package com.agileapes.couteau.context.event;

import com.agileapes.couteau.context.contract.Context;
import com.agileapes.couteau.context.contract.Event;

/**
 * This event is fired whenever a context has started up for the first time. Subsequent refreshings of the
 * context do not apply.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/29/13, 4:25 PM)
 */
public class ContextStartupEvent extends Event {

    private final Context<?> context;

    public ContextStartupEvent(Context<?> context) {
        super(context);
        this.context = context;
    }

    /**
     * Returns the context which has just been started up
     * @return the context
     */
    public Context<?> getContext() {
        return context;
    }

}
