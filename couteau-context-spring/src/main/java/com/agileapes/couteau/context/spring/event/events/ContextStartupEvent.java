package com.agileapes.couteau.context.spring.event.events;

import com.agileapes.couteau.context.contract.Context;
import com.agileapes.couteau.context.spring.event.TranslatedEvent;

/**
 * Translation for {@link com.agileapes.couteau.context.event.ContextStartupEvent}
 * 
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/29/13, 4:45 PM)
 */
public class ContextStartupEvent extends TranslatedEvent {

    private Context<?> context;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public ContextStartupEvent(Object source) {
        super(source);
    }

    public Context<?> getContext() {
        return context;
    }

}
