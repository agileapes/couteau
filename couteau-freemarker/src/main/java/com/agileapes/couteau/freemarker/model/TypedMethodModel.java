/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agileapes.couteau.freemarker.model;

import com.agileapes.couteau.basics.api.Filter;
import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.freemarker.api.Invokable;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import com.agileapes.couteau.reflection.util.assets.AnnotatedElementFilter;
import com.agileapes.couteau.reflection.util.assets.MethodReturnTypeFilter;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static com.agileapes.couteau.basics.collections.CollectionWrapper.with;
import static com.agileapes.couteau.reflection.util.ReflectionUtils.withMethods;

/**
 * This model allows for automatic type-matching for executed methods within a template
 * to run one of the (possibly many) methods available for execution as marked with
 * {@link Invokable}.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/31, 16:51)
 */
public class TypedMethodModel extends NativeMethodModel {

    private final Collection<Method> methods = new HashSet<Method>();

    public TypedMethodModel() {
        //noinspection unchecked
        withMethods(getClass())
        .keep(new AnnotatedElementFilter(Invokable.class))
        .drop(new MethodReturnTypeFilter(void.class))
        .each(new Processor<Method>() {
            @Override
            public void process(Method method) {
                methods.add(method);
            }
        });
    }

    @Override
    protected Object execute(final List<?> arguments) {
        //noinspection unchecked
        final List<Method> candidates = with(methods).keep(new Filter<Method>() {
            @Override
            public boolean accepts(Method item) {
                return item.getParameterTypes().length == arguments.size();
            }
        }).keep(new Filter<Method>() {
            @Override
            public boolean accepts(Method item) {
                Class<?>[] parameterTypes = item.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (arguments.get(i) != null && !ReflectionUtils.mapType(parameterTypes[i]).isInstance(arguments.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }).list();
        if (candidates.isEmpty()) {
            throw new Error("No candidate for execution of method with the given arguments");
        }
        if (candidates.size() > 1) {
            throw new Error("More than one candidate for execution of method with these arguments");
        }
        final Method method = candidates.get(0);
        method.setAccessible(true);
        final Object result;
        try {
            result = method.invoke(this, arguments.toArray());
        } catch (Exception e) {
            throw new Error("Failed to execute method", e);
        }
        return result;
    }

}
