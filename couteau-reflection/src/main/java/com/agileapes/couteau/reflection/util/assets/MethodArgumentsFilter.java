package com.agileapes.couteau.reflection.util.assets;

import com.agileapes.couteau.basics.api.Filter;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Filters methods by their arguments
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 5:35 PM)
 */
public class MethodArgumentsFilter implements Filter<Method> {

    private final Class[] arguments;

    public MethodArgumentsFilter(Class... arguments) {
        this.arguments = arguments;
    }

    @Override
    public boolean accepts(Method item) throws Exception {
        return Arrays.equals(arguments, item.getParameterTypes());
    }

}
