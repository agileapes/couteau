package com.agileapes.couteau.context.event;

import com.agileapes.couteau.context.contract.Context;
import com.agileapes.couteau.context.contract.Event;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/29/13, 4:25 PM)
 */
public class ContextStartupEvent extends Event {

    private final Context<?> context;

    public ContextStartupEvent(Context<?> context) {
        super(context);
        this.context = context;
    }

    public Context<?> getContext() {
        return context;
    }

}
