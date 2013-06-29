package com.agileapes.couteau.context.spring.event.events;

import com.agileapes.couteau.context.contract.Context;
import org.springframework.context.ApplicationEvent;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/29/13, 4:45 PM)
 */
public class ContextStartedEvent extends ApplicationEvent {

    private Context<?> context;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public ContextStartedEvent(Object source) {
        super(source);
    }

    public Context<?> getContext() {
        return context;
    }

}
