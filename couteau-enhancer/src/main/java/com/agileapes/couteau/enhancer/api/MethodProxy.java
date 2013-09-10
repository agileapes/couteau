package com.agileapes.couteau.enhancer.api;

/**
 * This interface allows for control over the flow of method execution
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/10, 14:53)
 */
public interface MethodProxy {

    /**
     * Falls back to calling the actual method implementation from the
     * enhanced class
     * @param target       the target of the call
     * @param arguments    the arguments to the call
     * @return the return value from the call
     * @throws Throwable
     */
    Object callSuper(Object target, Object[] arguments) throws Throwable;

}
