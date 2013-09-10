package com.agileapes.couteau.enhancer.api;

/**
 * The method interceptor will be called on at every juncture of the execution of the
 * methods of an enhanced entity. Only calls to final methods will not be intercepted
 * as they will not be wrapped.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/10, 14:48)
 */
public interface MethodInterceptor {

    /**
     * This method is called to indicate an interception of a method call
     * @param methodDescriptor    the descriptor indicating which method was called
     * @param target              the object that was the target of the method call
     * @param arguments           the arguments with which the call was carried out
     * @param methodProxy         the method proxy that allows control over the flow
     *                            of the method execution
     * @return the result of the method call. This will indicate the final result of
     * the method call.
     * @throws Throwable
     */
    Object intercept(MethodDescriptor methodDescriptor, Object target, Object[] arguments, MethodProxy methodProxy) throws Throwable;

}
