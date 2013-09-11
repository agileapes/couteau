package com.agileapes.couteau.enhancer.model;

import com.agileapes.couteau.freemarker.api.Invokable;
import com.agileapes.couteau.freemarker.model.FilteringMethodModel;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/11, 0:03)
 */
public class NonStaticMethodFilter extends FilteringMethodModel<Method> {

    @Invokable
    @Override
    protected boolean filter(Method item) {
        return !Modifier.isStatic(item.getModifiers());
    }

}
