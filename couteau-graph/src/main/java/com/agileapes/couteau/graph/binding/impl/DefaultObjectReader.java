package com.agileapes.couteau.graph.binding.impl;

import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.context.value.ValueReaderContext;
import com.agileapes.couteau.context.value.impl.DefaultValueReaderContext;
import com.agileapes.couteau.graph.binding.BindingType;
import com.agileapes.couteau.graph.binding.ObjectReader;
import com.agileapes.couteau.graph.binding.impl.api.Query;
import com.agileapes.couteau.graph.error.GraphBindingError;
import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.query.GraphNodePattern;
import com.agileapes.couteau.graph.query.NodePattern;
import com.agileapes.couteau.graph.tree.node.TreeNode;
import com.agileapes.couteau.reflection.beans.BeanInitializer;
import com.agileapes.couteau.reflection.beans.BeanWrapper;
import com.agileapes.couteau.reflection.beans.impl.ConstructorBeanInitializer;
import com.agileapes.couteau.reflection.beans.impl.FieldBeanWrapper;
import com.agileapes.couteau.reflection.error.BeanInstantiationException;
import com.agileapes.couteau.reflection.util.assets.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import static com.agileapes.couteau.reflection.util.ReflectionUtils.withFields;
import static com.agileapes.couteau.reflection.util.ReflectionUtils.withMethods;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
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
