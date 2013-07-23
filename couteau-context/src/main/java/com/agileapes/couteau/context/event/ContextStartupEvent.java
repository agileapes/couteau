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

package com.agileapes.couteau.context.event;

import com.agileapes.couteau.context.contract.Context;
import com.agileapes.couteau.context.contract.Event;

/**
 * This event is fired whenever a context has started up for the first time. Subsequent refreshings of the
 * context do not apply.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/29/13, 4:25 PM)
 */
public class ContextStartupEvent extends Event {

    private final Context<?> context;

    public ContextStartupEvent(Context<?> context) {
        super(context);
        this.context = context;
    }

    /**
     * Returns the context which has just been started up
     * @return the context
     */
    public Context<?> getContext() {
        return context;
    }

}
