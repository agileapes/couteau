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

package com.mmnaseri.couteau.context.value.impl;

import com.mmnaseri.couteau.context.error.RegistryException;
import com.mmnaseri.couteau.context.error.ValueReaderError;
import com.mmnaseri.couteau.context.impl.AbstractTypeSpecificContext;
import com.mmnaseri.couteau.context.impl.BeanProcessorAdapter;
import com.mmnaseri.couteau.context.value.ValueReader;
import com.mmnaseri.couteau.context.value.ValueReaderContext;
import com.agileapes.couteau.reflection.beans.BeanInitializer;
import com.agileapes.couteau.reflection.beans.impl.ConstructorBeanInitializer;
import com.agileapes.couteau.reflection.error.BeanInstantiationException;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import com.agileapes.couteau.reflection.util.assets.Modifiers;
import com.agileapes.couteau.strings.document.DocumentReader;
import com.agileapes.couteau.strings.document.impl.DefaultDocumentReader;
import com.agileapes.couteau.strings.document.impl.ListParser;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.regex.Pattern;

/**
 * This is the value reader context, the one place that aggregates all value readers and
 * is capable of routing the proper value readers for each type and delegate the reading.
 *
 * Do note that this value reader does not read any values in and of itself. Rather, it
 * can determine value readers who are capable of the task.
 *
 * For each type there may be more than one value reader, in which case they will be tried
 * -- without any particular ordering -- one by one until one of them reads the value properly.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/30, 14:06)
 */
public class DefaultValueReaderContext extends AbstractTypeSpecificContext<ValueReader> implements ValueReaderContext {

    /**
     * A set of supported target sets
     */
    @SuppressWarnings("unchecked")
    private static final Set<Class<?>> SUPPORTED_SETS = new HashSet<Class<?>>(
            Arrays.asList(HashSet.class, TreeSet.class)
    );

    /**
     * A set of supported target lists
     */
    @SuppressWarnings("unchecked")
    private static final Set<Class<?>> SUPPORTED_LISTS = new HashSet<Class<?>>(
            Arrays.asList(ArrayList.class)
    );

    /**
     * A set of supported target queues
     */
    @SuppressWarnings("unchecked")
    private static final Set<Class<?>> SUPPORTED_QUEUES = new HashSet<Class<?>>(
            Arrays.asList(PriorityQueue.class, ConcurrentLinkedQueue.class, LinkedBlockingDeque.class, LinkedList.class)
    );

    private final Map<Class<?>, Set<ValueReader>> cache = new ConcurrentHashMap<Class<?>, Set<ValueReader>>();
    private final BeanInitializer beanInitializer = new ConstructorBeanInitializer();

    public DefaultValueReaderContext() {
        addBeanProcessor(new BeanProcessorAdapter<ValueReader>() {
            @Override
            public ValueReader postProcessBeforeRegistration(ValueReader bean, String name) throws RegistryException {
                cache.clear();
                return bean;
            }
        });
        ready();
    }

    private Set<ValueReader> getCandidates(Class<?> type) {
        if (cache.containsKey(type)) {
            return cache.get(type);
        }
        final Set<ValueReader> candidates = new CopyOnWriteArraySet<ValueReader>();
        for (ValueReader reader : getBeans()) {
            if (reader.canRead(type)) {
                candidates.add(reader);
            }
        }
        cache.put(type, candidates);
        return candidates;
    }

    @Override
    public boolean canRead(Class<?> type) {
        return !getCandidates(type).isEmpty();
    }

    /**
     * <p>The textual value will be fed into value readers that are capable of reading the
     * value and is then translated.</p>
     *
     * <p>Note that feeding the value {@code "null"} as the text will result invariably in
     * the value being returned as {@code null}, without trying out any readers. To pass the
     * text {@code "null"} as the input, you will have to escape it thus: {@code "\null"}</p>
     * @param text    textual representation of the data
     * @param type    the type of data
     * @param <E>     the type parameter for the expected output
     * @return the value read by the registered value readers
     */
    @SuppressWarnings("unchecked")
    @Override
    public <E> E read(String text, Class<E> type) {
        return (E) read(text, (Type) type);
    }

    @SuppressWarnings("UnnecessaryUnboxing")
    private <E> Object readArray(Class<E> componentType, String text) {
        //noinspection unchecked
        final List<E> list = readCollection(List.class, componentType, text);
        final Object instance = Array.newInstance(componentType, list.size());
        for (int i = 0; i < list.size(); i++) {
            Object value = list.get(i);
            if (componentType.isPrimitive()) {
                if (short.class.equals(componentType)) {
                    value = ((Short) value).shortValue();
                } else if (int.class.equals(componentType)) {
                    value = ((Integer) value).intValue();
                } else if (long.class.equals(componentType)) {
                    value = ((Long) value).longValue();
                } else if (double.class.equals(componentType)) {
                    value = ((Double) value).doubleValue();
                } else if (float.class.equals(componentType)) {
                    value = ((Float) value).floatValue();
                } else if (char.class.equals(componentType)) {
                    value = ((Character) value).charValue();
                } else if (boolean.class.equals(componentType)) {
                    value = ((Boolean) value).booleanValue();
                }
            }
            Array.set(instance, i, value);
        }
        return instance;
    }

    private <E extends Collection<I>, I> E readCollection(Class<E> collectionType, Class<I> itemType, String text) {
        final E collection = getCollection(collectionType);
        final List<String> list = toList(text);
        for (String item : list) {
            collection.add(read(item, itemType));
        }
        return collection;
    }

    private <E extends Collection<I>, I> E getCollection(Class<E> collectionType) {
        final Collection<Class<?>> supportedTypes;
        if (Collection.class.equals(collectionType) || List.class.isAssignableFrom(collectionType)) {
            supportedTypes = getSupportedListTypes();
        } else if (Set.class.isAssignableFrom(collectionType)) {
            supportedTypes = getSupportedSetTypes();
        } else if (Queue.class.isAssignableFrom(collectionType)) {
            supportedTypes = getSupportedQueueTypes();
        } else {
            throw new IllegalStateException("No supported collection type could be found");
        }
        final Class<?> type = determineType(collectionType, supportedTypes);
        final E collection;
        try {
            collection = collectionType.cast(beanInitializer.initialize(type, new Class[0]));
        } catch (BeanInstantiationException e) {
            throw new IllegalStateException(e);
        }
        return collection;
    }

    private List<String> toList(String text) {
        final List<String> values = new ArrayList<String>();
        final DocumentReader documentReader = new DefaultDocumentReader(text);
        documentReader.skip(Pattern.compile("\\["));
        values.addAll(documentReader.parse(new ListParser(null, "\\]|$", "\"`'")));
        documentReader.skip(Pattern.compile("\\]"));
        return values;
    }

    /**
     * Determines a suitable, assignable type for the given input type. This is used for determining the
     * collection or map type to use.
     *
     * @param targetType     the expected target type, with which the returned type should be compatible
     * @param supportedTypes the supported types amongst which we must select a type
     * @return the supported, compatible type
     * @throws java.lang.IllegalStateException
     */
    private Class<?> determineType(Class<?> targetType, Collection<Class<?>> supportedTypes) {
        if (targetType.isInterface() || Modifiers.ABSTRACT.matches(targetType.getModifiers())) {
            for (Class<?> supportedType : supportedTypes) {
                if (targetType.isAssignableFrom(supportedType)) {
                    return supportedType;
                }
            }
            throw new IllegalStateException("Failed to determine a suitable type for: " + targetType.getCanonicalName());
        }
        return targetType;
    }

    /**
     * @return a collection of supported set types
     */
    protected Collection<Class<?>> getSupportedSetTypes() {
        return SUPPORTED_SETS;
    }

    /**
     * @return a collection of supported list types
     */
    protected Collection<Class<?>> getSupportedListTypes() {
        return SUPPORTED_LISTS;
    }

    /**
     * @return a collection of supported queue types
     */
    protected Collection<Class<?>> getSupportedQueueTypes() {
        return SUPPORTED_QUEUES;
    }

    @Override
    public Object read(String text, Type type) throws ValueReaderError {
        if (text == null || text.equals("null")) {
            return null;
        }
        if (text.equals("\\null")) {
            text = "null";
        }
        if (type instanceof ParameterizedType) {
            final ParameterizedType dataType = (ParameterizedType) type;
            if (Collection.class.isAssignableFrom((Class<?>) dataType.getRawType())) {
                Type argumentType = dataType.getActualTypeArguments()[0];
                if (argumentType instanceof ParameterizedType) {
                    argumentType = ((ParameterizedType) argumentType).getRawType();
                }
                return readCollection(((Class<?>) dataType.getRawType()).asSubclass(Collection.class), (Class<?>) argumentType, text);
            } else {
                return read(text, (Class<?>) dataType.getRawType());
            }
        } else {
            final Class<?> dataType = (Class<?>) type;
            if (dataType.isArray()) {
                return readArray(ReflectionUtils.getComponentType(dataType), text);
            }
            final Set<ValueReader> candidates = getCandidates(dataType);
            for (ValueReader candidate : candidates) {
                try {
                    return candidate.read(text, dataType);
                } catch (ValueReaderError ignored) {
                    //we ignore this to let other candidates have their go at the value
                }
            }
            throw new IllegalArgumentException("No reader capable of understanding <" + text + "> as an element of type <" + dataType.getCanonicalName() + "> was found.");
        }
    }

}
