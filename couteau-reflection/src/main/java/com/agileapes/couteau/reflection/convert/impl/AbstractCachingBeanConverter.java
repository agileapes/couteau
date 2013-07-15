package com.agileapes.couteau.reflection.convert.impl;

import com.agileapes.couteau.basics.api.Cache;
import com.agileapes.couteau.basics.api.impl.SimpleCache;
import com.agileapes.couteau.reflection.beans.*;
import com.agileapes.couteau.reflection.beans.impl.ConstructorBeanInitializer;
import com.agileapes.couteau.reflection.beans.impl.MethodBeanWrapperFactory;
import com.agileapes.couteau.reflection.convert.BeanConverter;
import com.agileapes.couteau.reflection.convert.ConversionDecision;
import com.agileapes.couteau.reflection.convert.ConversionStrategy;
import com.agileapes.couteau.reflection.convert.ConversionTarget;
import com.agileapes.couteau.reflection.error.BeanConversionException;
import com.agileapes.couteau.reflection.error.BeanInstantiationException;
import com.agileapes.couteau.reflection.error.FatalBeanConversionException;
import com.agileapes.couteau.reflection.property.PropertyDescriptor;
import com.agileapes.couteau.reflection.property.impl.SimplePropertyDescriptor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import com.agileapes.couteau.reflection.util.assets.Modifiers;

import javax.script.SimpleBindings;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 6:49 AM)
 */
public abstract class AbstractCachingBeanConverter implements BeanConverter {

    private final Cache<Map.Entry<Object, Class<?>>, Object> cache;
    private final BeanWrapperFactory wrapperFactory;
    private final BeanAccessorFactory accessorFactory;
    private final BeanInitializer initializer;
    private final ConversionStrategy conversionStrategy;

    @SuppressWarnings("unchecked")
    private static final Set<Class<?>> SUPPORTED_SETS = new HashSet<Class<?>>(
            Arrays.asList(HashSet.class, TreeSet.class)
    );
    @SuppressWarnings("unchecked")
    private static final Set<Class<?>> SUPPORTED_LISTS = new HashSet<Class<?>>(
            Arrays.asList(ArrayList.class)
    );

    @SuppressWarnings("unchecked")
    private static final Set<Class<?>> SUPPORTED_QUEUES = new HashSet<Class<?>>(
            Arrays.asList(PriorityQueue.class, ConcurrentLinkedQueue.class, LinkedBlockingDeque.class, LinkedList.class)
    );

    @SuppressWarnings("unchecked")
    private static final Set<Class<?>> SUPPORTED_MAPS = new HashSet<Class<?>>(
            Arrays.asList(HashMap.class, TreeMap.class, SimpleBindings.class, ConcurrentHashMap.class,
                    Hashtable.class)
    );

    protected AbstractCachingBeanConverter() {
        this(new DefaultConversionStrategy());
    }

    public AbstractCachingBeanConverter(ConversionStrategy conversionStrategy) {
        this(new MethodBeanWrapperFactory(), conversionStrategy);
    }

    public AbstractCachingBeanConverter(final BeanWrapperFactory wrapperFactory, ConversionStrategy conversionStrategy) {
        this(new BeanAccessorFactory() {
            @Override
            public <E> BeanAccessor<E> getBeanAccessor(E bean) {
                return wrapperFactory.getBeanWrapper(bean);
            }
        }, wrapperFactory, conversionStrategy);
    }

    public AbstractCachingBeanConverter(BeanAccessorFactory accessorFactory, BeanWrapperFactory wrapperFactory, ConversionStrategy conversionStrategy) {
        this.accessorFactory = accessorFactory;
        this.wrapperFactory = wrapperFactory;
        this.initializer = new ConstructorBeanInitializer();
        this.cache = new SimpleCache<Map.Entry<Object, Class<?>>, Object>();
        this.conversionStrategy = conversionStrategy;
    }

    @Override
    public <I, O> O convert(I bean, Class<O> targetType) throws BeanConversionException {
        final AbstractMap.SimpleEntry<Object, Class<?>> key = new AbstractMap.SimpleEntry<Object, Class<?>>(bean, targetType);
        if (cache.contains(key)) {
            //noinspection unchecked
            return (O) cache.read(key);
        }
        final BeanAccessor<I> source = accessorFactory.getBeanAccessor(bean);
        final O targetInstance;
        try {
            //noinspection unchecked
            targetInstance = (O) initializer.initialize(ReflectionUtils.mapType(targetType), new Class[0]);
        } catch (BeanInstantiationException e) {
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

    protected abstract void doConvert(BeanAccessor<?> source, BeanWrapper<?> target) throws BeanConversionException;

    protected Object convertProperty(PropertyDescriptor descriptor, Class<?> targetType, ConversionDecision decision) throws BeanConversionException {
        if (descriptor.isNull()) {
            return null;
        }
        if (decision.equals(ConversionDecision.IGNORE)) {
            return null;
        }
        if (decision.equals(ConversionDecision.PASS) && ReflectionUtils.mapType(targetType).isInstance(descriptor.getValue())) {
            return descriptor.getValue();
        }
        final Object result;
        if (Collection.class.isAssignableFrom(descriptor.getType())) {
            //noinspection unchecked
            Class<? extends Collection> collectionType = (Class<? extends Collection>) targetType;
            result = convertCollection((Collection<?>) descriptor.getValue(), collectionType, ReflectionUtils.resolveTypeArguments(descriptor.getGenericType(), 1)[0]);
        } else if (Map.class.isAssignableFrom(descriptor.getType())) {
            //noinspection unchecked
            Class<? extends Map> mapType = (Class<? extends Map>) targetType;
            final Class[] types = ReflectionUtils.resolveTypeArguments(descriptor.getGenericType(), 2);
            result = convertMap((Map<?, ?>) descriptor.getValue(), mapType, types[0], types[1]);
        } else {
            result = convert(descriptor.getValue(), targetType);
        }
        return result;
    }

    protected ConversionStrategy getConversionStrategy() {
        return conversionStrategy;
    }

    private Collection convertCollection(Collection<?> original, Class<? extends Collection> collectionType, Class<?> itemType) throws BeanConversionException {
        final Class type;
        if (Set.class.isAssignableFrom(collectionType)) {
            type = determineType(collectionType, getSupportedSetTypes());
        } else if (List.class.isAssignableFrom(collectionType)) {
            type = determineType(collectionType, getSupportedListTypes());
        } else if (Queue.class.isAssignableFrom(collectionType)) {
            type = determineType(collectionType, getSupportedQueueTypes());
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

    protected Collection<Class<?>> getSupportedSetTypes() {
        return SUPPORTED_SETS;
    }

    protected Collection<Class<?>> getSupportedListTypes() {
        return SUPPORTED_LISTS;
    }

    protected Collection<Class<?>> getSupportedQueueTypes() {
        return SUPPORTED_QUEUES;
    }

    protected Collection<Class<?>> getSupportedMapTypes() {
        return SUPPORTED_MAPS;
    }

}
