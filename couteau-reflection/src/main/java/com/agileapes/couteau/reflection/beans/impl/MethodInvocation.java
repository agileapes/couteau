package com.agileapes.couteau.reflection.beans.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/7/13, 6:00 PM)
 */
public class MethodInvocation {

    private final Method method;
    private final Object target;
    private final Object[] arguments;

    public MethodInvocation(Method method, Object target, Object... arguments) {
        this.method = method;
        this.target = target;
        this.arguments = arguments;
    }

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
