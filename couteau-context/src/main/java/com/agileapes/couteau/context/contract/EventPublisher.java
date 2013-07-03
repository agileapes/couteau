package com.agileapes.couteau.context.contract;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/29, 13:56)
 */
public interface EventPublisher {

    <E extends Event> E publishEvent(E event);

    /**
     * Will add the provided event listener to this event publisher
     * @param eventListener    the event listener
     */
    void addEventListener(EventListener<? extends Event> eventListener);
}
