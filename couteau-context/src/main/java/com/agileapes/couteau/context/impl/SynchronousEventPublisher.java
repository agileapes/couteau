package com.agileapes.couteau.context.impl;

import com.agileapes.couteau.context.contract.Event;
import com.agileapes.couteau.context.contract.EventListener;
import com.agileapes.couteau.context.contract.EventPublisher;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This is the default implementation for the {@link EventPublisher} interface, which will synchronously
 * publish events according to listener ordering and return the results to the triggering entity.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/22/13, 7:43 AM)
 */
public class SynchronousEventPublisher implements EventPublisher {

    /**
     * The list of all listeners
     */
    private final List<SmartEventListener> eventListeners = new CopyOnWriteArrayList<SmartEventListener>();

    /**
     * Publishes a synchronous event to the listeners. The event must inherit from {@link Event}, ensuring
     * that it specifies its source and can be tracked back to it.
     * @param event    the event object being fired
     * @param <E>      the type of the event
     * @return the (modified) event object.
     * <p>There are cases in which it would be necessary to allow event listeners to modify the mutable properties
     * of an event object, which can be later on used to steer the flow of work through the context from which the
     * event was raised.</p>
     * <p>This could be used in a variety of ways; for instance, to redirect an erroneous input to a closely
     * matching one that holds meaning with the context.</p>
     */
    @Override
    public <E extends Event> E publishEvent(E event) {
        for (SmartEventListener listener : eventListeners) {
            if (listener.supportsEvent(event)) {
                listener.onEvent(event);
            }
        }
        return event;
    }

    /**
     * Will add the provided event listener to this event publisher
     * @param eventListener    the event listener
     */
    @Override
    public void addEventListener(EventListener<? extends Event> eventListener) {
        SmartEventListener smartEventListener;
        if (eventListener instanceof SmartEventListener) {
            smartEventListener = (SmartEventListener) eventListener;
        } else {
            //noinspection unchecked
            smartEventListener = new SmartEventListener((EventListener<Event>) eventListener);
        }
        eventListeners.add(smartEventListener);
        Collections.sort(eventListeners, new OrderedBeanComparator());
    }

}
