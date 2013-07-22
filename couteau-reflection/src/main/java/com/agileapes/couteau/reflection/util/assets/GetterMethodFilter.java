package com.agileapes.couteau.reflection.util.assets;

import com.agileapes.couteau.basics.api.Filter;

import java.lang.reflect.Method;

/**
 * Accepts all getter methods
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 3:44 PM)
 */
public class GetterMethodFilter implements Filter<Method> {

    @Override
    public boolean accepts(Method item) throws Exception {
        return Modifiers.PUBLIC.matches(item) && !Modifiers.ABSTRACT.matches(item) && !Modifiers.STATIC.matches(item)
                && !item.getReturnType().equals(void.class) && item.getParameterTypes().length == 0 &&
                (item.getName().matches("get[A-Z].*") || (item.getName().matches("is[A-Z].*") && item.getReturnType().equals(boolean.class)));
    }

}
