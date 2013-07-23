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

package com.agileapes.couteau.context.spring.event.impl;

import com.agileapes.couteau.context.contract.Event;
import com.agileapes.couteau.reflection.util.ClassUtils;

/**
 * This translator will translate all events in {@code couteau-context} into their counterparts understandable by Spring
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/30, 10:28)
 */
public class BuiltInEventsTranslationScheme extends AbstractMappedEventsTranslationScheme {

    public static final String BUILTIN_EVENTS_PACKAGE = "com.agileapes.couteau.context.event.";
    public static final String TRANSLATED_EVENTS_PACKAGE = "com.agileapes.couteau.context.spring.event.events.";

    @Override
    public boolean handles(Event event) {
        return event.getClass().getCanonicalName().startsWith(BUILTIN_EVENTS_PACKAGE);
    }

    @Override
    protected Class<?> mapEvent(Class<? extends Event> eventClass, ClassLoader classLoader) throws ClassNotFoundException {
        return ClassUtils.forName(TRANSLATED_EVENTS_PACKAGE.concat(eventClass.getSimpleName()), classLoader);
    }

}
