package com.agileapes.couteau.reflection.property.impl;

import com.agileapes.couteau.reflection.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * This implementation allows for easily writing read and write accessor through methods.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:31 PM)
 */
public abstract class AbstractMethodPropertyAccessor<E> extends AbstractPropertyAccessor<E> {

    /**
     * The method being wrapped
     */
    private final Method method;

    /**
     * The target of invocation
     */
    private Object target;

    /**
     * Instantiates the accessor through the method
     * @param method    the method
     * @param target    the object being targeted
     */
    public AbstractMethodPropertyAccessor(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    /**
     * Invokes the wrapped method
     * @param arguments    the arguments to pass to the method
     * @return the value returned by the method
     * @throws Exception
     */
    protected Object invoke(Object... arguments) throws Exception {
        return method.invoke(target, arguments);
    }

    /**
     * @return the method wrapped by the accessor
     */
    protected Method getMethod() {
        return method;
    }

    /**
     * @return the method's invocation target
     */
    protected Object getTarget() {
        return target;
    }

    /**
     * @return the type of the object wrapped
     */
    protected Class<?> getTargetType() {
        return getTarget().getClass();
    }

    /**
     * Changes the target object
     * @param target    the target to be used for invocation
     */
    public void setTarget(Object target) {
        this.target = target;
    }

    /**
     * @return the generic type of the property being wrapped
     */
    @Override
    public Type getGenericPropertyType() {
        return getMethod().getGenericReturnType();
    }

    /**
     * Returns the simple name of the underlying member or constructor
     * represented by this Member.
     *
     * @return the simple name of the underlying member
     */
    @Override
    public String getName() {
        return ReflectionUtils.getPropertyName(getMethod().getName());
    }

    @Override
    protected AnnotatedElement getAnnotatedElement() {
        return getMethod();
    }

    @Override
    protected Member getMember() {
        return getMethod();
    }
}
