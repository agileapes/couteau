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

/**
 * A reconfigurable context is a context that can be reconfigured and then refreshed. The semantics of this context
 * requires that you refresh it after reconfiguring.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:25 PM)
 */
public interface ReconfigurableContext<E> extends Context<E> {

    /**
     * This method will cause the context to be refreshed. Note that this means that the startup
     * process for the context will be repeated. This has the side-effect that all {@link ContextProcessor}s
     * will run again over the context.
     */
    void refresh();

}
