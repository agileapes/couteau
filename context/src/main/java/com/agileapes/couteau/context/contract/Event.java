package com.agileapes.couteau.context.contract;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/29, 13:56)
 */
public abstract class Event {

    private final Object source;

    public Event(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }

}
