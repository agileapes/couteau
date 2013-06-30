package com.agileapes.couteau.context.spring.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/30, 10:13)
 */
public abstract class TranslatedEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public TranslatedEvent(Object source) {
        super(source);
    }

}
