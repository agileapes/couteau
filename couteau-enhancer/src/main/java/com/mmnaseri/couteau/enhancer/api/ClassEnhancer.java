/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mmnaseri.couteau.enhancer.api;

/**
 * <p>This interface encapsulates the behaviour of an enhancer. The class enhancer will
 * take a super class and generate byte code for a class that is an extension to the
 * specified class, and is in addition a client for the specified interfaces.</p>
 *
 * <p>The generated class will <em>always</em> implement {@link Interceptible}. This is
 * to allow for easy injection of interceptors.</p>
 *
 * <p>You can specify interfaces through {@link #setInterfaces(Class[])}</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/9/10, 15:09)
 */
public interface ClassEnhancer<E> {

    /**
     * Tells the enhancer which implementation of the super type we will be extending
     * @param superClass    the super type for the extended class
     */
    void setSuperClass(Class<? extends E> superClass);

    /**
     * Tells the enhancer which interfaces the enhanced class should implement
     * @param interfaces    the interfaces to implement
     */
    void setInterfaces(Class[] interfaces);

    /**
     * Changes the naming policy for the enhancement process
     * @param namingPolicy    the naming policy
     */

    void setNamingPolicy(NamingPolicy namingPolicy);

    /**
     * Enhances the byte code for the given class to generate a class object that
     * will allow for creation of beans that implement interfaces additional to those
     * implemented by the original superclass.
     * @return the enhanced class
     */
    Class<? extends E> enhance();

}
