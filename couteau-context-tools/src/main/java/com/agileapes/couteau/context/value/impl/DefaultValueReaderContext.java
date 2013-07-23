/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.couteau.context.value.impl;

import com.agileapes.couteau.context.error.RegistryException;
import com.agileapes.couteau.context.error.ValueReaderError;
import com.agileapes.couteau.context.impl.AbstractTypeSpecificContext;
import com.agileapes.couteau.context.impl.BeanProcessorAdapter;
import com.agileapes.couteau.context.value.ValueReader;
import com.agileapes.couteau.context.value.ValueReaderContext;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

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

    private final Map<Class<?>, Set<ValueReader>> cache = new ConcurrentHashMap<Class<?>, Set<ValueReader>>();

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
    @Override
    public <E> E read(String text, Class<E> type) {
        if (text == null || text.equals("null")) {
            return null;
        }
        if (text.equals("\\null")) {
            text = "null";
        }
        final Set<ValueReader> candidates = getCandidates(type);
        for (ValueReader candidate : candidates) {
            try {
                return candidate.read(text, type);
            } catch (ValueReaderError ignored) {
                //we ignore this to let other candidates have their go at the value
            }
        }
        throw new IllegalArgumentException("No reader capable of understanding <" + text + "> was found.");
    }

}
