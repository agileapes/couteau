/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
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

package com.mmnaseri.couteau.context.spring.event.impl;

import com.mmnaseri.couteau.context.contract.Event;
import com.mmnaseri.couteau.context.spring.event.TranslatedEvent;

import java.util.Enumeration;
import java.util.Properties;

/**
 * This is a generic event that has a set of properties.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (6/29/13, 4:29 PM)
 */
public class GenericApplicationEvent extends TranslatedEvent {

    private final Properties properties = new Properties();
    private final Event originalEvent;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source            the component that published the event (never {@code null})
     * @param originalEvent     the event originally raised by Couteau
     */
    public GenericApplicationEvent(Object source, Event originalEvent) {
        super(source);
        this.originalEvent = originalEvent;
    }

    public Enumeration<?> getPropertyNames() {
        return properties.propertyNames();
    }

    public Object getProperty(String name) {
        return properties.get(name);
    }

    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    public Event getOriginalEvent() {
        return originalEvent;
    }
}
