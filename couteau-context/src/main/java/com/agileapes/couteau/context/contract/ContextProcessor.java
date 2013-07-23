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
 * The context processor will be allowed access to the context to which it belongs with
 * a single call. The context processor might be called more than once, if the context is refreshed.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:12 PM)
 */
public interface ContextProcessor<E> {

    /**
     * This method will be called whenever the context has been readied
     * @param context    the context to be processed
     */
    void postProcessContext(Context<E> context);

}
