/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.couteau.context.contract;

import java.io.Serializable;

/**
 * The event class is the base of all events in the system. This is quite convenient since
 * events will have to declare the source of their rising. This follows in the footsteps of
 * {@link java.util.EventObject}.
 *
 * The event is also serializable, allowing it to be serialized to be multi-cast across a wider
 * range of platforms than the vanilla JRE over which the current application is running.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/29, 13:56)
 */
public abstract class Event implements Serializable {

    private final Object source;

    /**
     * Instantiates the event object
     * @param source    the source of the event (the place from which it was published through
     *                  an event publisher)
     */
    public Event(Object source) {
        this.source = source;
    }

    /**
     * @return the source of the event
     */
    public Object getSource() {
        return source;
    }

}
