package com.agileapes.couteau.reflection.beans.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * This class will encapsulate the process of invoking a method on a given object given specific
 * arguments. This is to store invocation information for a later time so that we could reproduce
 * the process of invoking a given method with a given state input readily.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/7/13, 6:00 PM)
 */
public class MethodInvocation {

    /**
     * The method to be invoked
     */
    private final Method method;

    /**
     * The target on which the method is being invoked
     */
    private final Object target;

    /**
     * The arguments for the method invocation.
     */
    private final Object[] arguments;

    public MethodInvocation(Method method, Object target, Object... arguments) {
        this.method = method;
        this.target = target;
        this.arguments = arguments;
    }

    /**
     * Invokes the method on the given target using the given arguments.
     * @return the value returned by the method.
     * @throws InvocationTargetException
     */
    public Object invoke() throws InvocationTargetException {
        method.setAccessible(true);
        try {
            return method.invoke(target, arguments);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodInvocation that = (MethodInvocation) o;
        return Arrays.equals(arguments, that.arguments) && method.equals(that.method) && target.equals(that.target);
    }

    @Override
    public int hashCode() {
        int result = method.hashCode();
        result = 31 * result + target.hashCode();
        result = 31 * result + Arrays.hashCode(arguments);
        return result;
    }

}
