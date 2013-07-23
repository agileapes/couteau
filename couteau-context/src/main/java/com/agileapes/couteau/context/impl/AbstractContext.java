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

package com.agileapes.couteau.context.impl;

import com.agileapes.couteau.context.contract.*;
import com.agileapes.couteau.context.contract.EventListener;
import com.agileapes.couteau.context.error.FatalRegistryException;
import com.agileapes.couteau.context.error.RegistryException;
import com.agileapes.couteau.context.event.ContextRefreshedEvent;
import com.agileapes.couteau.context.event.ContextStartupEvent;
import com.agileapes.couteau.reflection.util.ClassUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>This implementation offers the semantics of a reconfigurable context, extending {@link AbstractRegistry}
 * and leaving all operations unimplemented by that class to be realized by extending classes.</p>
 *
 * <p>Please note that the context will need to be refreshed before it can be used. Alternatively, if
 * the readiness of the context holds a semantic meaning with the extending classes, they can call
 * to {@link #ready()} to signal that the context is ready to be used.</p>
 *
 * <p>This context will pick up event listeners, context aware beans, and context processors from
 * among the beans registered with it.</p>
 *
 * <p>All beans implementing {@link ContextAware} will be given the context as a property injection
 * if they indicate that they require access to a context of items that is at least a super-type of
 * the items in the context.</p>
 *
 * <p>This means that if this is a context for objects of type {@code A} which extends type {@code B}, you can
 * gain access to this context by extending {@code ContextAware<A>}, {@code ContextAware<B>},
 * {@code ContextAware<Object>}, or simply {@code ContextAware}. However, you cannot access
 * the context through {@code ContextAware<D>} where {@code D} is a sub-type of {@code C}.</p>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:19 PM)
 */
public abstract class AbstractContext<E> extends AbstractRegistry<E> implements ReconfigurableContext<E> {

    /**
     * This is a list of bean processors for the beans.
     */
    private final List<BeanProcessor<E>> beanProcessors = new CopyOnWriteArrayList<BeanProcessor<E>>();

    /**
     * This is the list of context processors.
     */
    private final List<ContextProcessor<E>> contextProcessors = new CopyOnWriteArrayList<ContextProcessor<E>>();

    /**
     * The startup date. The context is considered to be not ready so long as this parameter is
     * {@code null}
     * @see #isContextReady()
     */
    private Date startupDate = null;

    /**
     * The time it took the system to start up since the context was instantiated.
     */
    private long startupTime = System.currentTimeMillis();

    /**
     * The type of items in the context
     */
    private final Class<E> contextType;

    /**
     * The event publisher that carries out the task of publishing events throughout this context.
     */
    private final EventPublisher eventPublisher;

    /**
     * This constructor instantiates the context, while adding several bean processors that will
     * allow the context to work properly.
     */
    public AbstractContext() {
        //noinspection unchecked
        contextType = (Class<E>) ClassUtils.resolveTypeArgument(getClass(), AbstractContext.class);
        eventPublisher = new SynchronousEventPublisher();
        //This post processor will inject the context into eligible, requesting beans
        addBeanProcessor(new BeanProcessorAdapter<E>(OrderedBean.HIGHEST_PRECEDENCE) {
            @Override
            public E postProcessBeforeRegistration(E bean, String name) throws RegistryException {
                if (bean instanceof ContextAware) {
                    final ContextAware contextAware = (ContextAware) bean;
                    final Class<?> contextType = ClassUtils.resolveTypeArgument(contextAware.getClass(), ContextAware.class);
                    final Class<?> currentType = getRegistryType();
                    if ((contextType == null && currentType.equals(Object.class)) || (contextType != null && contextType.isAssignableFrom(currentType))) {
                        //noinspection unchecked
                        contextAware.setContext(AbstractContext.this);
                    }
                }
                return bean;
            }
        });
        //This processor will recognize event listeners
        addBeanProcessor(new BeanProcessorAdapter<E>(OrderedBean.HIGHEST_PRECEDENCE + 1) {
            @Override
            public E postProcessBeforeRegistration(E bean, String name) throws RegistryException {
                if (bean instanceof EventListener<?>) {
                    addEventListener((EventListener<?>) bean);
                }
                return bean;
            }
        });
        //This processor will recognize context processors and add them whenever they are
        //registered with the context
        addBeanProcessor(new BeanProcessorAdapter<E>(OrderedBean.HIGHEST_PRECEDENCE + 2) {
            @Override
            public E postProcessBeforeRegistration(E bean, String name) throws RegistryException {
                if (bean instanceof ContextProcessor) {
                    ContextProcessor processor = (ContextProcessor) bean;
                    final Class<?> contextType = ClassUtils.resolveTypeArgument(processor.getClass(), ContextProcessor.class);
                    final Class<E> currentType = getRegistryType();
                    if ((contextType == null && currentType.equals(Object.class)) || (contextType != null && contextType.isAssignableFrom(currentType))) {
                        //noinspection unchecked
                        addContextProcessor(processor);
                    }
                }
                return bean;
            }
        });
        publishEvent(new ContextStartupEvent(this));
    }

    /**
     * Will add the context processor. Calling this method will cause the context to be
     * marked as unprepared. To use the context to retrieve items, you will need to
     * call {@link #refresh()}
     * @param processor    the processor requesting access to the context
     * @return the context
     */
    public Context<E> addContextProcessor(ContextProcessor<E> processor) {
        contextProcessors.add(processor);
        markUnprepared();
        return this;
    }

    /**
     * Will mark the context as unprepared. After calling this method, the context needs to be
     * refreshed for the user to be able to access its contents.
     */
    protected void markUnprepared() {
        startupDate = null;
    }

    /**
     * This method will add bean processors to the context. Bean processors, since the moment they
     * are added to the system, will be able to manipulate the process with which the beans
     * ae registered with the underlying registry.
     * @param processor    the processor
     * @return the context itself. This is returned for chaining purposes.
     */
    @Override
    public Context<E> addBeanProcessor(BeanProcessor<E> processor) {
        beanProcessors.add(processor);
        return this;
    }

    /**
     * @return the time in milliseconds in which the context was set up and readied for use.
     */
    @Override
    public long getStartupTime() {
        if (!isContextReady()) {
            throw new IllegalStateException("Context has not started up");
        }
        return startupTime;
    }

    /**
     * @return the date object representing the moment at which the context was marked as ready
     * for use
     */
    @Override
    public Date getStartupDate() {
        if (!isContextReady()) {
            throw new IllegalStateException("Context has not started up. You must refresh the context before use.");
        }
        return startupDate;
    }

    /**
     * <p>This method will mark the context as being ready. This should be typically called from an extending context
     * that holds some semantics for when and how the beans registered with the context can be used.</p>
     *
     * <p>Without signaling the context, the context cannot be used externally.</p>
     */
    protected synchronized void ready() {
        //If the context has already been marked as ready, it cannot be prepared for use again.
        if (isContextReady()) {
            throw new IllegalStateException("Context startup cannot happen more than once");
        }
        //since the context is now ready, we can call to post processors that operate on the context itself
        for (ContextProcessor<E> contextProcessor : contextProcessors) {
            contextProcessor.postProcessContext(this);
        }
        //setting up context startup date and time
        startupDate = new Date();
        startupTime = System.currentTimeMillis() - startupTime;
    }

    /**
     * Will register the given item with the given name. The name must be unique, and no other
     * item with this name must be already registered.
     * @param name    the name of the bean to be registered
     * @param item    the instance
     * @throws RegistryException
     */
    @Override
    public void register(String name, E item) throws RegistryException {
        if (!contextProcessors.isEmpty() && isContextReady()) {
            markUnprepared();
        }
        if (item instanceof NamedBean) {
            ((NamedBean) item).setName(name);
        }
        item = postProcessBeanBeforeRegistration(name, item);
        super.register(name, item);
    }

    /**
     * Will return the bean with the given name. If the bean is not present, an exception will
     * be thrown.
     * @param name    the name of the bean being queried.
     * @return the bean instance
     * @throws RegistryException
     */
    @Override
    public E get(String name) throws RegistryException {
        if (!isContextReady()) {
            throw new IllegalStateException("Context not started up");
        }
        E item = super.get(name);
        item = postProcessBeanBeforeAccess(name, item);
        return item;
    }

    /**
     * Will determines whether the context is now ready to be used or not.
     * @return {@code true} if the context is ready to be used externally
     */
    private boolean isContextReady() {
        return startupDate != null;
    }

    /**
     * This method acts as a composite that runs all post processors that access the bean before
     * its registration sequentially and according to their order.
     * @param name    the name of the bean
     * @param item    the bean itself
     * @return the (possibly modified) bean, which cannot be {@code null}
     * @throws RegistryException
     */
    private E postProcessBeanBeforeRegistration(String name, E item) throws RegistryException {
        final ArrayList<BeanProcessor<E>> beanProcessors = new ArrayList<BeanProcessor<E>>(this.beanProcessors);
        Collections.sort(beanProcessors, new OrderedBeanComparator());
        for (BeanProcessor<E> processor : beanProcessors) {
            item = processor.postProcessBeforeRegistration(item, name);
            if (item == null) {
                throw new FatalRegistryException("Item cannot be nullified by a post processor");
            }
        }
        return item;
    }

    /**
     * This method acts as a composite that sequentially  and according to their order runs all post processors
     * that access the bean before it is being accessed externally.
     * @param name    the name of the bean
     * @param item    the bean itself
     * @return the (possibly modified) bean, which cannot be {@code null}
     * @throws RegistryException
     */
    private E postProcessBeanBeforeAccess(String name, E item) throws RegistryException {
        final ArrayList<BeanProcessor<E>> beanProcessors = new ArrayList<BeanProcessor<E>>(this.beanProcessors);
        Collections.sort(beanProcessors, new OrderedBeanComparator());
        for (BeanProcessor<E> processor : beanProcessors) {
            item = processor.postProcessBeforeAccess(item, name);
            if (item == null) {
                throw new FatalRegistryException("Item cannot be nullified by a post processor");
            }
        }
        return item;
    }

    /**
     * Refreshes the context so that changes made to the context can be taken into account
     */
    @Override
    public void refresh() {
        markUnprepared();
        ready();
        publishEvent(new ContextRefreshedEvent(this));
    }

    /**
     * Will return a transform of beans with their names as keys that are the subtype of the given
     * type
     * @param type    the desired type for which the lookup will be performed
     * @param <T>     the type argument
     * @return a transform of bean names to bean instances for the given type
     * @throws RegistryException
     */
    @Override
    public <T extends E> Map<String, T> getBeansOfType(Class<T> type) throws RegistryException {
        final Collection<String> beanNames = getBeanNames();
        final HashMap<String, T> map = new HashMap<String, T>();
        for (String beanName : beanNames) {
            final E bean = get(beanName);
            if (type.isInstance(bean)) {
                //noinspection unchecked
                map.put(beanName, (T) bean);
            }
        }
        return map;
    }

    /**
     * Will register a bean with the context, determining the name automatically
     * @param item    the item to be registered
     * @return the context itself
     * @throws RegistryException
     */
    @Override
    public Context<E> register(E item) throws RegistryException {
        final String canonicalName = item.getClass().getCanonicalName();
        String name = canonicalName == null ? item.getClass().toString().replaceAll("\\s+", "") : canonicalName;
        name = name.trim() + "#" + (getBeanNames().size() + 1);
        register(name, item);
        return this;
    }

    /**
     * Will add the provided event listener to this context, which also acts as an event publisher itself
     * @param eventListener    the event listener
     */
    @Override
    public void addEventListener(EventListener<? extends Event> eventListener) {
        eventPublisher.addEventListener(eventListener);
    }

    /**
     * Publishes a synchronous event to the listeners. The event must inherit from {@link Event}, ensuring
     * that it specifies its source and can be tracked back to it.
     * @param event    the event object being fired
     * @param <E>      the type of the event
     * @return the (modified) event object.
     * <p>There are cases in which it would be necessary to allow event listeners to modify the mutable properties
     * of an event object, which can be later on used to steer the flow of work through the context from which the
     * event was raised.</p>
     * <p>This could be used in a variety of ways; for instance, to redirect an erroneous input to a closely
     * matching one that holds meaning with the context.</p>
     */
    @Override
    public <E extends Event> E publishEvent(E event) {
        return eventPublisher.publishEvent(event);
    }

    /**
     * Determines the narrowest supertype of objects that can be registered with this registry.
     * @return the type of objects in the registry
     */
    @Override
    public Class<E> getRegistryType() {
        return contextType;
    }

}
