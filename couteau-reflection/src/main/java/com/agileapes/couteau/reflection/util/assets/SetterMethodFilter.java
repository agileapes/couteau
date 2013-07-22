package com.agileapes.couteau.reflection.util.assets;

import com.agileapes.couteau.basics.api.Filter;

import java.lang.reflect.Method;

/**
 * Accepts all setter methods
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 3:53 PM)
 */
public class SetterMethodFilter implements Filter<Method> {

    @Override
    public boolean accepts(Method item) throws Exception {
        return Modifiers.PUBLIC.matches(item) && !Modifiers.ABSTRACT.matches(item) && !Modifiers.STATIC.matches(item)
                && item.getReturnType().equals(void.class) && item.getParameterTypes().length == 1 && item.getName().matches("set[A-Z].*");
    }

}
