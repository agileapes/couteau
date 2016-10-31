/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mmnaseri.couteau.context.impl;

import com.mmnaseri.couteau.context.contract.Event;
import com.mmnaseri.couteau.context.contract.EventListener;
import com.mmnaseri.couteau.context.contract.EventPublisher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This is the default implementation for the {@link EventPublisher} interface, which will synchronously
 * publish events according to listener ordering and return the results to the triggering entity.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
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
        synchronized (this) {
            eventListeners.add(smartEventListener);
            final List<SmartEventListener> listeners = new ArrayList<SmartEventListener>(eventListeners);
            Collections.sort(listeners, new OrderedBeanComparator());
            eventListeners.clear();
            eventListeners.addAll(listeners);
        }
    }

}
