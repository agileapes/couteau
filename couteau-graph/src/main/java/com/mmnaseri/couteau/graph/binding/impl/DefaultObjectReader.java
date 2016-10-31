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

package com.mmnaseri.couteau.graph.binding.impl;

import com.mmnaseri.couteau.basics.api.Processor;
import com.mmnaseri.couteau.context.value.ValueReaderContext;
import com.mmnaseri.couteau.context.value.impl.DefaultValueReaderContext;
import com.mmnaseri.couteau.graph.binding.BindingType;
import com.mmnaseri.couteau.graph.binding.ObjectReader;
import com.mmnaseri.couteau.graph.binding.impl.api.Query;
import com.mmnaseri.couteau.graph.error.GraphBindingError;
import com.mmnaseri.couteau.graph.node.Node;
import com.mmnaseri.couteau.graph.query.GraphNodePattern;
import com.mmnaseri.couteau.graph.query.NodePattern;
import com.mmnaseri.couteau.graph.tree.node.TreeNode;
import com.mmnaseri.couteau.reflection.beans.BeanInitializer;
import com.mmnaseri.couteau.reflection.beans.BeanWrapper;
import com.mmnaseri.couteau.reflection.beans.impl.ConstructorBeanInitializer;
import com.mmnaseri.couteau.reflection.beans.impl.FieldBeanWrapper;
import com.mmnaseri.couteau.reflection.error.BeanInstantiationException;
import com.mmnaseri.couteau.reflection.util.assets.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import static com.mmnaseri.couteau.reflection.util.ReflectionUtils.withFields;
import static com.mmnaseri.couteau.reflection.util.ReflectionUtils.withMethods;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (14/2/17 AD, 18:24)
 */
public class DefaultObjectReader<N extends Node> implements ObjectReader<N> {

    public static final String COMPILE_METHOD_NAME = "compile";
    public static final Class<? extends NodePattern> DEFAULT_PATTERN_CLASS = GraphNodePattern.class;
    private final BeanInitializer beanInitializer = new ConstructorBeanInitializer();
    private final ValueReaderContext valueReaderContext;
    private final Method compileMethod;

    public DefaultObjectReader() {
        this(DEFAULT_PATTERN_CLASS);
    }

    public <E extends NodePattern> DefaultObjectReader(Class<E> patternClass) {
        this(new DefaultValueReaderContext(), patternClass);
    }

    public <E extends NodePattern> DefaultObjectReader(ValueReaderContext valueReaderContext) {
        this(valueReaderContext, GraphNodePattern.class);
    }

    public <E extends NodePattern> DefaultObjectReader(ValueReaderContext valueReaderContext, Class<E> patternClass) {
        this.valueReaderContext = valueReaderContext;
        this.compileMethod = withMethods(patternClass)
                .keep(new MemberModifierFilter(Modifiers.STATIC))
                .keep(new MemberNameFilter(COMPILE_METHOD_NAME))
                .keep(new MethodArgumentsFilter(String.class))
                .keep(new MethodReturnTypeFilter(NodePattern.class))
                .first();
        if (compileMethod == null) {
            throw new IllegalStateException("Class " + patternClass.getCanonicalName() + " has no method <public static " + NodePattern.class.getCanonicalName() + " compile(String)>");
        }
    }

    @Override
    public <E> E read(Class<E> type, final N node) throws GraphBindingError {
        final E bean;
        try {
            bean = beanInitializer.initialize(type, new Class[0]);
        } catch (BeanInstantiationException e) {
            throw new GraphBindingError("Failed to initialize bean using the default constructor", e);
        }
        final BeanWrapper<E> beanWrapper = new FieldBeanWrapper<E>(bean);
        //noinspection unchecked
        withFields(type).keep(new AnnotatedElementFilter(Query.class)).each(new Processor<Field>() {
            @Override
            public void process(Field field) {
                final Query annotation = field.getAnnotation(Query.class);
                final NodePattern pattern;
                try {
                    pattern = (NodePattern) compileMethod.invoke(null, annotation.value());
                } catch (Exception e) {
                    throw new GraphBindingError("Failed to compile pattern: " + annotation.value(), e);
                }
                final List<N> matches = pattern.finder(node).find();
                if (matches.isEmpty()) {
                    return;
                }
                if (Collection.class.isAssignableFrom(field.getType())) {
                } else {
                    if (matches.size() > 1) {
                        throw new GraphBindingError("More than one element matches query " + annotation.value() + " for field " + field.getName());
                    }
                    final N found = matches.get(0);
                    final Object value;
                    if (BindingType.ATTRIBUTE.equals(annotation.type())) {
                        value = valueReaderContext.read(found.getAttribute(annotation.target()), field.getType());
                    } else if (BindingType.INDEX.equals(annotation.type())) {
                        value = valueReaderContext.read(found instanceof TreeNode ? Integer.toString(((TreeNode) found).getNodeIndex()) : "-1", field.getType());
                    } else if (BindingType.VALUE.equals(annotation.type())) {
                        throw new UnsupportedOperationException();
                    } else if (BindingType.ELEMENT.equals(annotation.type())) {
                        value = read(field.getType(), found);
                    } else {
                        value = null;
                    }
                    try {
                        beanWrapper.setPropertyValue(field.getName(), value);
                    } catch (Exception e) {
                        throw new GraphBindingError("Failed to set property " + field.getName(), e);
                    }
                }
            }
        });
        return bean;
    }

}
