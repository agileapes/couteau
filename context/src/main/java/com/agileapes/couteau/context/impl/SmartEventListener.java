package com.agileapes.couteau.context.impl;

import com.agileapes.couteau.context.contract.Event;
import com.agileapes.couteau.context.contract.EventListener;
import com.agileapes.couteau.context.util.ClassUtils;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/29, 14:01)
 */
public class SmartEventListener implements EventListener {

    private final EventListener<Event> delegate;
    private final Class<?> eventType;

    public SmartEventListener(EventListener<Event> delegate) {
        this.delegate = delegate;
        this.eventType = ClassUtils.resolveTypeArgument(delegate.getClass(), EventListener.class);
    }

    public boolean supportsEvent(Event event) {
        return eventType.isAssignableFrom(event.getClass());
    }

    @Override
    public void onEvent(Event event) {
        delegate.onEvent(event);
    }

}
