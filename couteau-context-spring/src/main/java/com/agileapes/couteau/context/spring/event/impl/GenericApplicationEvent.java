package com.agileapes.couteau.context.spring.event.impl;

import com.agileapes.couteau.context.spring.event.TranslatedEvent;

import java.util.Enumeration;
import java.util.Properties;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/29/13, 4:29 PM)
 */
public class GenericApplicationEvent extends TranslatedEvent {

    private final Properties properties = new Properties();

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public GenericApplicationEvent(Object source) {
        super(source);
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

}
