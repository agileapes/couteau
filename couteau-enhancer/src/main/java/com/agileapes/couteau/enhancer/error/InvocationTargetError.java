package com.agileapes.couteau.enhancer.error;

import com.agileapes.couteau.enhancer.api.MethodDescriptor;

/**
 * Indicates an error in the execution of an enhanced method
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/10, 15:05)
 */
public class InvocationTargetError extends Error {

    public InvocationTargetError(MethodDescriptor method, Throwable cause) {
        super("There was an error calling " + method.getName(), cause);
    }

}
