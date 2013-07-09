package com.agileapes.couteau.reflection.property.impl;

import com.agileapes.couteau.reflection.beans.impl.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:31 PM)
 */
public abstract class AbstractMethodPropertyAccessor<E> extends AbstractPropertyAccessor<E> {

    private final MethodInvocation methodInvocation;
    private final Method method;
    private final Object target;

    public AbstractMethodPropertyAccessor(String propertyName, Class<E> propertyType, Method method, Object target) {
        super(propertyName, propertyType);
        this.method = method;
        this.target = target;
        methodInvocation = new MethodInvocation(method, target);
    }

    protected Object invoke(Object... arguments) throws Exception {
        return methodInvocation.invoke();
    }

    protected Method getMethod() {
        return method;
    }

    protected Object getTarget() {
        return target;
    }

    protected Class<?> getTargetType() {
        return target.getClass();
    }

}
