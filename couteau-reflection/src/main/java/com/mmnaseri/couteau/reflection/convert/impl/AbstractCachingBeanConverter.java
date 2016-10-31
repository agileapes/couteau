/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
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

package com.mmnaseri.couteau.reflection.convert.impl;

import com.mmnaseri.couteau.basics.api.Cache;
import com.mmnaseri.couteau.basics.api.impl.SimpleCache;
import com.mmnaseri.couteau.reflection.beans.*;
import com.mmnaseri.couteau.reflection.beans.impl.BeanAccessorFactoryAdapter;
import com.mmnaseri.couteau.reflection.beans.impl.ConstructorBeanInitializer;
import com.mmnaseri.couteau.reflection.beans.impl.MethodBeanWrapperFactory;
import com.mmnaseri.couteau.reflection.convert.BeanConverter;
import com.mmnaseri.couteau.reflection.convert.ConversionDecision;
import com.mmnaseri.couteau.reflection.convert.ConversionStrategy;
import com.mmnaseri.couteau.reflection.convert.ConversionTarget;
import com.mmnaseri.couteau.reflection.error.BeanConversionException;
import com.mmnaseri.couteau.reflection.error.BeanFactoryException;
import com.mmnaseri.couteau.reflection.error.BeanInstantiationException;
import com.mmnaseri.couteau.reflection.error.FatalBeanConversionException;
import com.mmnaseri.couteau.reflection.property.PropertyDescriptor;
import com.mmnaseri.couteau.reflection.property.impl.SimplePropertyDescriptor;
import com.mmnaseri.couteau.reflection.util.ReflectionUtils;
import com.mmnaseri.couteau.reflection.util.assets.Modifiers;

import javax.script.SimpleBindings;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * <p>This is an abstract bean converter that knows how to do everything, except reading properties from the input
 * bean and writing values to the output bean.</p>
 * <p/>
 * <p>This is to be handled by extending classes through {@link #doConvert(BeanAccessor, BeanWrapper)}.</p>
 * <p/>
 * <p>Converters extending this abstract implementation will benefit from the caching it provides. This means
 * that cyclic references will not cause any problems, and moreover, no conversion will be carried out more
 * than once.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/13/13, 6:49 AM)
 */
public abstract class AbstractCachingBeanConverter implements BeanConverter {

    /**
     * The cache holding converted beans
     */
    private final Cache<Map.Entry<Object, Class<?>>, Object> cache;

    /**
     * The wrapper factory for target beans
     */
    private final BeanWrapperFactory wrapperFactory;

    /**
     * The accessor factory for input beans
     */
    private final BeanAccessorFactory accessorFactory;

    /**
     * The bean initializer for target beans
     */
    private final BeanInitializer initializer;

    /**
     * The conversion strategy
     */
    private final ConversionStrategy conversionStrategy;

    private final BeanFactory beanFactory;

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

    /**
     * A set of supported target maps
     */
    @SuppressWarnings("unchecked")
    private static final Set<Class<?>> SUPPORTED_MAPS = new HashSet<Class<?>>(
            Arrays.asList(HashMap.class, TreeMap.class, SimpleBindings.class, ConcurrentHashMap.class,
                    Hashtable.class)
    );

    /**
     * Instantiates the converter with the default strategy ({@link DefaultConversionStrategy})
     *
     * @see #AbstractCachingBeanConverter(ConversionStrategy)
     */
    public AbstractCachingBeanConverter() {
        this(new DefaultConversionStrategy());
    }

    /**
     * Instantiates the converter using the given strategy, defaulting to a method bean wrapper factory
     * ({@link MethodBeanWrapperFactory})
     *
     * @param conversionStrategy the conversion strategy to be used throughout the conversion
     * @see #AbstractCachingBeanConverter(BeanWrapperFactory, ConversionStrategy)
     */
    public AbstractCachingBeanConverter(ConversionStrategy conversionStrategy) {
        this(new MethodBeanWrapperFactory(), conversionStrategy);
    }

    /**
     * Instantiates the converter using the given wrapper factory and the given strategy. In this case, the
     * wrapper factory will act as the accessor factory, as well.
     *
     * @param wrapperFactory     the wrapper factory
     * @param conversionStrategy the conversion strategy to be used throughout the conversion
     * @see #AbstractCachingBeanConverter(BeanFactory, BeanAccessorFactory, BeanWrapperFactory, ConversionStrategy)
     */
    public AbstractCachingBeanConverter(BeanWrapperFactory wrapperFactory, ConversionStrategy conversionStrategy) {
        this(null, new BeanAccessorFactoryAdapter(wrapperFactory), wrapperFactory, conversionStrategy);
    }

    /**
     * Instantiates the converter using the given parameters.
     *
     * @param beanFactory the factory through which new bean instances will be created when needed
     */
    public AbstractCachingBeanConverter(BeanFactory beanFactory) {
        this(beanFactory, new DefaultConversionStrategy());
    }

    /**
     * Instantiates the converter using the given parameters.
     *
     * @param beanFactory        the factory through which new bean instances will be created when needed
     * @param conversionStrategy the conversion strategy to be used throughout the conversion
     */
    public AbstractCachingBeanConverter(BeanFactory beanFactory, ConversionStrategy conversionStrategy) {
        this(beanFactory, new MethodBeanWrapperFactory(), conversionStrategy);
    }

    /**
     * Instantiates the converter using the given parameters.
     *
     * @param beanFactory        the factory through which new bean instances will be created when needed
     * @param wrapperFactory     the wrapper factory for output objects
     * @param conversionStrategy the conversion strategy to be used throughout the conversion
     */
    public AbstractCachingBeanConverter(BeanFactory beanFactory, BeanWrapperFactory wrapperFactory, ConversionStrategy conversionStrategy) {
        this(beanFactory, new BeanAccessorFactoryAdapter(wrapperFactory), wrapperFactory, conversionStrategy);
    }

    /**
     * Instantiates the converter using the given parameters.
     *
     * @param beanFactory        the factory through which new bean instances will be created when needed
     * @param accessorFactory    the accessor factory for input objects
     * @param wrapperFactory     the wrapper factory for output objects
     * @param conversionStrategy the conversion strategy to be used throughout the conversion
     */
    public AbstractCachingBeanConverter(BeanFactory beanFactory, BeanAccessorFactory accessorFactory, BeanWrapperFactory wrapperFactory, ConversionStrategy conversionStrategy) {
        this.accessorFactory = accessorFactory;
        this.wrapperFactory = wrapperFactory;
        this.initializer = new ConstructorBeanInitializer();
        this.cache = new SimpleCache<Map.Entry<Object, Class<?>>, Object>();
        this.conversionStrategy = conversionStrategy;
        this.beanFactory = beanFactory;
    }

    /**
     * This method will convert the input bean into an instance of the specified target type.
     *
     * @param bean       the input bean
     * @param targetType target type
     * @param <I>        the input type
     * @param <O>        the output type
     * @return converted bean
     * @throws BeanConversionException
     */
    @Override
    public <I, O> O convert(I bean, Class<O> targetType) throws BeanConversionException {
        if (bean == null) {
            return null;
        }
        final AbstractMap.SimpleEntry<Object, Class<?>> key = new AbstractMap.SimpleEntry<Object, Class<?>>(bean, targetType);
        if (cache.contains(key)) {
            //noinspection unchecked
            return (O) cache.read(key);
        }
        final BeanAccessor<I> source = accessorFactory.getBeanAccessor(bean);
        final O targetInstance;
        try {
            //noinspection unchecked
            targetInstance = beanFactory.getBean(ReflectionUtils.mapType(targetType));
        } catch (BeanFactoryException e) {
            throw new FatalBeanConversionException("Failed to instantiate bean of type: " + targetType.getCanonicalName(), e);
        }
        if (targetInstance instanceof ConversionTarget) {
            ConversionTarget target = (ConversionTarget) targetInstance;
            target.setBean(bean);
        }
        final BeanWrapper<O> target = wrapperFactory.getBeanWrapper(targetInstance);
        doConvert(source, target);
        cache.write(key, target.getBean());
        return target.getBean();
    }

    /**
     * This method is expected to carry out the task of conversion by converting values from
     * properties read from the source object into the destination object
     *
     * @param source the accessor for the input bean
     * @param target the wrapper for the target bean
     * @throws BeanConversionException
     */
    protected abstract void doConvert(BeanAccessor<?> source, BeanWrapper<?> target) throws BeanConversionException;

    /**
     * This method will convert a given property, based on the given decision
     *
     * @param descriptor the property descriptor
     * @param decision   the decision
     * @return the converted value, or {@code null} if the decision was {@code IGNORE}
     * @throws BeanConversionException
     */
    protected Object convertProperty(PropertyDescriptor descriptor, ConversionDecision decision) throws BeanConversionException {
        if (descriptor.isNull()) {
            return null;
        }
        if (decision.equals(ConversionDecision.IGNORE)) {
            return null;
        }
        if (decision.equals(ConversionDecision.PASS) ||
                (!Collection.class.isAssignableFrom(descriptor.getType()) && !Map.class.isAssignableFrom(descriptor.getType()) &&
                        ReflectionUtils.mapType(descriptor.getType()).isInstance(descriptor.getValue()))) {
            return descriptor.getValue();
        }
        final Object result;
        if (descriptor.getType().isArray()) {
            result = convertArray(descriptor);
        } else if (Collection.class.isAssignableFrom(descriptor.getType())) {
            //noinspection unchecked
            Class<? extends Collection> collectionType = (Class<? extends Collection>) descriptor.getType();
            result = convertCollection((Collection<?>) descriptor.getValue(), collectionType, ReflectionUtils.resolveTypeArguments(descriptor.getGenericType(), 1)[0]);
        } else if (Map.class.isAssignableFrom(descriptor.getType())) {
            //noinspection unchecked
            Class<? extends Map> mapType = (Class<? extends Map>) descriptor.getType();
            final Class[] types = ReflectionUtils.resolveTypeArguments(descriptor.getGenericType(), 2);
            result = convertMap((Map<?, ?>) descriptor.getValue(), mapType, types[0], types[1]);
        } else {
            result = convert(descriptor.getValue(), descriptor.getType());
        }
        return result;
    }

    /**
     * Converts an array into another array
     *
     * @param descriptor the property descriptor
     * @return the converted array
     * @throws BeanConversionException
     */
    private Object convertArray(PropertyDescriptor descriptor) throws BeanConversionException {
        final int expected = ReflectionUtils.getArrayDimensions(descriptor.getType());
        final int actual = ReflectionUtils.getArrayDimensions(descriptor.getValue().getClass());
        if (expected != actual) {
            throw new FatalBeanConversionException("Array dimensions do not match: actual: " + actual + ", expected: " + expected);
        }
        return doConvertArray(ReflectionUtils.getComponentType(descriptor.getType()), (Object[]) descriptor.getValue());
    }

    /**
     * This method will recursively handle the conversion of the array
     *
     * @param type  the type of the components of the array
     * @param value the value of the array being converted
     * @return the converted array
     * @throws BeanConversionException
     */
    private Object[] doConvertArray(Class<?> type, Object[] value) throws BeanConversionException {
        final int[] dimensions = new int[ReflectionUtils.getArrayDimensions(value.getClass())];
        dimensions[0] = value.length;
        final Object[] converted = (Object[]) Array.newInstance(type, dimensions);
        for (int i = 0; i < value.length; i++) {
            Object item = value[i];
            if (item == null) {
                converted[i] = null;
            } else if (item.getClass().isArray()) {
                converted[i] = doConvertArray(type, (Object[]) item);
            } else {
                final ConversionDecision decision = getConversionStrategy().decide(new SimplePropertyDescriptor("", type, type, item));
                if (decision.equals(ConversionDecision.IGNORE)) {
                    converted[i] = null;
                } else if (decision.equals(ConversionDecision.PASS) || (ReflectionUtils.mapType(type).isAssignableFrom(ReflectionUtils.getComponentType(item.getClass())))) {
                    converted[i] = item;
                } else if (decision.equals(ConversionDecision.CONVERT)) {
                    converted[i] = convert(item, type);
                }
            }
        }


        return converted;
    }

    /**
     * @return the conversion strategy used with this converter
     */
    protected ConversionStrategy getConversionStrategy() {
        return conversionStrategy;
    }

    /**
     * Converts a collection of items
     *
     * @param original       the original collection
     * @param collectionType the type of the target collection
     * @param itemType       the types of the items in the target collection
     * @return the converted collection
     * @throws BeanConversionException
     */
    private Collection convertCollection(Collection<?> original, Class<? extends Collection> collectionType, Class<?> itemType) throws BeanConversionException {
        final Class type;
        if (Set.class.isAssignableFrom(collectionType)) {
            type = determineType(collectionType, getSupportedSetTypes());
        } else if (List.class.isAssignableFrom(collectionType)) {
            type = determineType(collectionType, getSupportedListTypes());
        } else if (Queue.class.isAssignableFrom(collectionType)) {
            type = determineType(collectionType, getSupportedQueueTypes());
        } else if (Collection.class.equals(collectionType)) {
            type = determineType(ArrayList.class, getSupportedListTypes());
        } else {
            throw new FatalBeanConversionException("Unsupported collection type: " + collectionType);
        }
        final Collection collection;
        try {
            collection = (Collection) initializer.initialize(type, new Class[0]);
        } catch (BeanInstantiationException e) {
            throw new FatalBeanConversionException("Failed to instantiate bean of type: " + type.getCanonicalName());
        }
        for (Object item : original) {
            final ConversionDecision decision = conversionStrategy.decide(new SimplePropertyDescriptor("", item.getClass(), item.getClass()));
            if (decision.equals(ConversionDecision.CONVERT)) {
                //noinspection unchecked
                collection.add(convert(item, itemType));
            } else {
                //noinspection unchecked
                collection.add(item);
            }
        }
        return collection;
    }

    /**
     * Converts an input map into a target map
     *
     * @param original the original map
     * @param mapType  the target map type
     * @param keyType  the target key type
     * @param itemType the target value type
     * @return the converted map
     * @throws BeanConversionException
     */
    private Map convertMap(Map original, Class<? extends Map> mapType, Class<?> keyType, Class<?> itemType) throws BeanConversionException {
        final Class type = determineType(mapType, getSupportedMapTypes());
        final Map map;
        try {
            map = (Map) initializer.initialize(type, new Class[0]);
        } catch (BeanInstantiationException e) {
            throw new FatalBeanConversionException("Failed to instantiate bean of type: " + type.getCanonicalName());
        }
        for (Object item : original.entrySet()) {
            final Map.Entry entry = (Map.Entry) item;
            //noinspection unchecked
            map.put(convert(entry.getKey(), keyType), convert(entry.getValue(), itemType));
        }
        return map;
    }

    /**
     * Determines a suitable, assignable type for the given input type. This is used for determining the
     * collection or map type to use.
     *
     * @param targetType     the expected target type, with which the returned type should be compatible
     * @param supportedTypes the supported types amongst which we must select a type
     * @return the supported, compatible type
     * @throws BeanConversionException
     */
    private Class<?> determineType(Class<?> targetType, Collection<Class<?>> supportedTypes) throws BeanConversionException {
        if (targetType.isInterface() || Modifiers.ABSTRACT.matches(targetType.getModifiers())) {
            for (Class<?> supportedType : supportedTypes) {
                if (targetType.isAssignableFrom(supportedType)) {
                    return supportedType;
                }
            }
            throw new FatalBeanConversionException("Failed to determine a suitable type for: " + targetType.getCanonicalName());
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

    /**
     * @return a collection of supported map types
     */
    protected Collection<Class<?>> getSupportedMapTypes() {
        return SUPPORTED_MAPS;
    }

}
