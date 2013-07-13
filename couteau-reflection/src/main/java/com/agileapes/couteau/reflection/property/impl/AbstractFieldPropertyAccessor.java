package com.agileapes.couteau.reflection.property.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/9/13, 12:38 PM)
 */
public abstract class AbstractFieldPropertyAccessor<E> extends AbstractPropertyAccessor<E> {

    private final Field field;
    private Object target;

    public AbstractFieldPropertyAccessor(String propertyName, Class<E> propertyType, Field field, Object target) {
        super(propertyName, propertyType);
        this.field = field;
        this.target = target;
    }

    protected Field getField() {
        return field;
    }

    protected Object getTarget() {
        return target;
    }

    protected Class<?> getTargetType() {
        return target.getClass();
    }

    protected Object get() throws Exception {
        getField().setAccessible(true);
        return getField().get(getTarget());
    }

    protected void set(Object value) throws Exception {
        getField().setAccessible(true);
        getField().set(getTarget(), value);
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public Type getGenericPropertyType() {
        return getField().getGenericType();
    }
}
