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

package com.mmnaseri.couteau.reflection.beans.impl;

import com.mmnaseri.couteau.basics.api.Builder;
import com.mmnaseri.couteau.basics.api.Transformer;
import com.mmnaseri.couteau.reflection.beans.BeanFactory;
import com.mmnaseri.couteau.reflection.error.BeanFactoryException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>This is a bean factory that will delegate all instantiations to a delegate bean factory,
 * unless a bean mapping has been specified by passing in transformers.</p>
 *
 * <p>For instance, suppose that we want all beans to be automatically instantiated through an instance of
 * {@link InitializerBeanFactory} while instances of a fictional class, {@code MyClass} with the given constructor
 * will be instantiated manually:</p>
 *
 * <code><pre>
 *
 *     public class MyClass {
 *
 *         //:
 *
 *         public MyClass(int x, float y) {
 *             // do something here
 *         }
 *
 *         //:
 *
 *     }
 *
 * </pre></code>
 *
 * <p>To achieve this, we will simply write a mapping through the builder:</p>
 *
 * <code><pre>
 *
 *     final BeanFactory factory = TypedBeanFactory.newBuilder(new InitializerBeanFactory()).map(MyClass.class, new Transformer&lt;Class&lt;? extends MyClass&gt;, MyClass&gt;() {
 *         public MyClass map&lt;Class&lt;? extends MyClass&gt;, MyClass&gt;(Class&lt;? extends MyClass&gt; beanType) {
 *             return new MyClass(10, 20.0);
 *         }
 *     }).build();
 *
 * </pre></code>
 *
 * <p>Now, when calling to the instantiated {@code factory}'s {@code getBean()} method, we will see that all beans
 * are instantiated through their default constructor, while instances of {@code MyClass} are created through the
 * mapping specified manually.</p>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/18 AD, 14:46)
 */
public class TypedBeanFactory implements BeanFactory {

    private final Map<Class<?>, Transformer<?, ?>> transformers;
    private final BeanFactory beanFactory;

    private TypedBeanFactory(Map<Class<?>, Transformer<?, ?>> transformers, BeanFactory delegateBeanFactory) {
        this.transformers = transformers;
        this.beanFactory = delegateBeanFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(Class<? extends T> beanType) throws BeanFactoryException {
        for (Class<?> convertibleType : transformers.keySet()) {
            if (convertibleType.isAssignableFrom(beanType)) {
                final Transformer transformer = transformers.get(convertibleType);
                final Object instance = transformer.map(beanType);
                if (instance == null) {
                    continue;
                }
                return (T) instance;
            }
        }
        return beanFactory.getBean(beanType);
    }

    /**
     * Creates a TypedBeanFactoryBuilder using the {@link InitializerBeanFactory default delegate bean factory}.
     * @return the bean factory builder
     */
    public static TypedBeanFactoryBuilder newBuilder() {
        return newBuilder(new InitializerBeanFactory());
    }

    /**
     * Creates a TypedBeanFactoryBuilder using the provided delegate bean factory
     * @param delegateBeanFactory   the bean factory which will be responsible of instantiating beans that are
     *                              not mapped specifically using {@link TypedBeanFactory.TypedBeanFactoryBuilder#map(Class, Transformer)}
     * @return the bean factory builder
     */
    public static TypedBeanFactoryBuilder newBuilder(InitializerBeanFactory delegateBeanFactory) {
        return new TypedBeanFactoryBuilder(delegateBeanFactory);
    }

    public static class TypedBeanFactoryBuilder implements Builder<BeanFactory> {

        private final Map<Class<?>, Transformer<?, ?>> transformers;
        private final InitializerBeanFactory delegateBeanFactory;

        private TypedBeanFactoryBuilder(InitializerBeanFactory delegateBeanFactory) {
            this(Collections.<Class<?>, Transformer<?, ?>>emptyMap(), delegateBeanFactory);
        }

        public TypedBeanFactoryBuilder(Map<Class<?>, Transformer<?, ?>> transformers, InitializerBeanFactory delegateBeanFactory) {
            this.delegateBeanFactory = delegateBeanFactory;
            this.transformers = new HashMap<Class<?>, Transformer<?, ?>>(transformers);
        }

        /**
         * Adds a new mapping for a type. Using this method, any time an instance of a given bean of the specified type
         * is required, the designated transformer will be called on to provide the instance.
         * @param beanType       the type of the bean
         * @param transformer    the transformer in charge of instantiating the bean
         * @param <E>            the type parameter for the bean
         * @return the builder itself.
         */
        public <E> TypedBeanFactoryBuilder map(Class<? extends E> beanType, Transformer<Class<? extends E>, E> transformer) {
            transformers.put(beanType, transformer);
            return new TypedBeanFactoryBuilder(transformers, delegateBeanFactory);
        }

        @Override
        public BeanFactory build() {
            return new TypedBeanFactory(transformers, delegateBeanFactory);
        }

    }

}
