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

package com.agileapes.couteau.reflection.convert;

/**
 * This enum abstracts a decision at conversion time.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:32 AM)
 */
public enum  ConversionDecision {

    /**
     * This means that the item being contemplated should be passed to the target object
     * as-is, without any conversion
     */
    PASS,
    /**
     * This means that the item being contemplated should be dropped and not included in the
     * target object
     */
    IGNORE,
    /**
     * This means that a deep conversion should be queued for the given object
     */
    CONVERT

}
