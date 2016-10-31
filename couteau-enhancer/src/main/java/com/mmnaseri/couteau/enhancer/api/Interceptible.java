/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
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
 * This interface indicates that the implementing Java bean will be accepting
 * interceptors. This is the interface implemented by all enhanced objects through
 * this framework.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/9/10, 15:11)
 */
public interface Interceptible {

    /**
     * Injects the interceptor into the interceptible object
     * @param interceptor    the interceptor
     */
    void setInterceptor(MethodInterceptor interceptor);

}
