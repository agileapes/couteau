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

package com.agileapes.couteau.enhancer.model;

import com.mmnaseri.couteau.basics.api.Filter;
import com.agileapes.couteau.freemarker.api.Invokable;
import com.agileapes.couteau.freemarker.model.TypedMethodModel;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static com.mmnaseri.couteau.basics.collections.CollectionWrapper.with;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/7/6 AD, 16:23)
 */
public class MethodPickerMethod extends TypedMethodModel {

    @Invokable
    public Collection<Method> pick(Collection<Method> source) {
        final ArrayList<Method> result = new ArrayList<Method>();
        for (final Method method : source) {
            final Filter<Method> filter = new Filter<Method>() {
                @Override
                public boolean accepts(Method evaluated) {
                    return method.getName().endsWith(evaluated.getName()) && Arrays.equals(method.getParameterTypes(), evaluated.getParameterTypes());
                }
            };
            final Method found = with(result).find(filter);
            if (found == null) {
                result.add(method);
            } else {
                result.remove(found);
                result.add(pickMethod(found, method));
            }
        }
        return result;
    }

    /**
     * Between two getter methods defined for a given property, both declared in the same class, picks one
     * @param first     the first getter
     * @param second    the second getter
     * @return the winning getter
     */
    private static Method pickMethod(Method first, Method second) {
        //if one is an interface, and one is not, the concrete implementation wins
        if (Modifier.isInterface(first.getModifiers()) && !Modifier.isInterface(second.getModifiers())) {
            return second;
        }
        if (!Modifier.isInterface(first.getModifiers()) && Modifier.isInterface(second.getModifiers())) {
            return first;
        }
        //if one is abstract and the other is not, the non-abstract one wins
        if (Modifier.isAbstract(first.getModifiers()) && !Modifier.isAbstract(second.getModifiers())) {
            return second;
        }
        if (!Modifier.isAbstract(first.getModifiers()) && Modifier.isAbstract(second.getModifiers())) {
            return first;
        }
        //given a hierarchy, the child wins
        if (first.getReturnType().isAssignableFrom(second.getReturnType())) {
            return second;
        }
        if (second.getReturnType().isAssignableFrom(first.getReturnType())) {
            return first;
        }
        return first;
    }

}
