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

package com.mmnaseri.couteau.context.contract;

/**
 * <p>This interface encapsulates the process of publishing events throughout a system, following the publisher-subscriber
 * (pub-sub) pattern.</p>
 *
 * <p>While this interface denotes the publisher, the subscribers can subscribe to this event publisher using
 * {@link #addEventListener(EventListener)}</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/6/29, 13:56)
 */
public interface EventPublisher {

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
    <E extends Event> E publishEvent(E event);

    /**
     * Will add the provided event listener to this event publisher
     * @param eventListener    the event listener
     */
    void addEventListener(EventListener<? extends Event> eventListener);
}
