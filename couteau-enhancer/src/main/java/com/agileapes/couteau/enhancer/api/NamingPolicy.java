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

package com.agileapes.couteau.enhancer.api;

/**
 * This interface allows for deciding how the name of an enhanced class will be decided
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/10, 15:18)
 */
public interface NamingPolicy {

    /**
     * This method is called when it is necessary to decide on the name of a newly
     * enhanced class
     * @param originalClass    the original class that is enhanced to form the newly
     *                         enhanced subclass
     * @param classEnhancer    the enhancer that is calling to this method for a new
     *                         name for the class
     * @return the name of the class
     */
    String getClassName(Class<?> originalClass, ClassEnhancer<?> classEnhancer);

}