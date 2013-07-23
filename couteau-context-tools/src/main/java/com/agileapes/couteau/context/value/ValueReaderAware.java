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

package com.agileapes.couteau.context.value;

/**
 * This is a convenience interface designed to let underlying systems to inject the value
 * reader context into beans without exposing it as a context, to minimize access and maximize
 * object security.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/3/13, 11:43 AM)
 */
public interface ValueReaderAware {

    /**
     * Method for injecting value readers into beans
     * @param valueReader    the value reader
     */
    void setValueReader(ValueReader valueReader);

}
