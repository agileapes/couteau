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

package com.agileapes.couteau.enhancer.api;

/**
 * This interface will allow for creation of enhanced objects.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/10, 14:56)
 */
public interface Enhancer<E> {

    /**
     * Changes the superclass of the enhanced classes
     * @param superClass    the superclass
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
     * Changes the interceptor that will be injected into method
     * calls of the enhanced object
     * @param interceptor    the interceptor
     */
    void setInterceptor(MethodInterceptor interceptor);

    /**
     * @return an instance of the enhanced object created via the default constructor
     */
    E create();

    /**
     * @param argumentTypes           the types of the parameters
     * @param constructorArguments    the arguments to the constructor
     * @return an instance of the enhanced object via a constructor matching the given
     * argument types
     */
    E create(Class[] argumentTypes, Object[] constructorArguments);

}
