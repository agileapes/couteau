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

package com.agileapes.couteau.basics.api.impl;

import com.agileapes.couteau.basics.api.Stringifiable;

/**
 * This implementation allows for compile time decision of the textual representation for an object that
 * uses stringifiable instances
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/31/13, 10:47 AM)
 */
public class StaticStringifiable<T> implements Stringifiable<T> {

    private final String representation;

    public StaticStringifiable(String representation) {
        this.representation = representation;
    }

    @Override
    public String toString(T object) {
        return representation;
    }

}
