package com.agileapes.couteau.context.contract;

/**
 * Event listeners are hooks that can be put in place so that they are made aware of the
 * occurrence of certain events.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/29, 13:58)
 */
public interface EventListener<E extends Event> {

    /**
     * This method will be called whenever the requested event has been triggered through a
     * context
     * @param event    the event being fired
     */
    void onEvent(E event);

}
