package com.agileapes.couteau.context.spring.event.events;

import com.agileapes.couteau.context.contract.Context;
import com.agileapes.couteau.context.spring.event.TranslatedEvent;

/**
 * The Spring translation for {@link com.agileapes.couteau.context.event.ContextRefreshedEvent}
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/29/13, 5:13 PM)
 */
public class ContextRefreshedEvent extends TranslatedEvent {

    private Context<?> context;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public ContextRefreshedEvent(Object source) {
        super(source);
    }

    public Context<?> getContext() {
        return context;
    }

}
