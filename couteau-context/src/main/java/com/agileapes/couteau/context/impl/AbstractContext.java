package com.agileapes.couteau.context.impl;

import com.agileapes.couteau.context.contract.*;
import com.agileapes.couteau.context.contract.EventListener;
import com.agileapes.couteau.context.error.FatalRegistryException;
import com.agileapes.couteau.context.error.RegistryException;
import com.agileapes.couteau.context.event.ContextRefreshedEvent;
import com.agileapes.couteau.context.event.ContextStartupEvent;
import com.agileapes.couteau.context.util.ClassUtils;

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
     * The list of all listeners
     */
    private final List<SmartEventListener> eventListeners = new CopyOnWriteArrayList<SmartEventListener>();
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
     * This constructor instantiates the context, while adding several bean processors that will
     * allow the context to work properly.
     */
    public AbstractContext() {
        //noinspection unchecked
        contextType = (Class<E>) ClassUtils.resolveTypeArgument(getClass(), AbstractContext.class);
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

    @Override
    public Context<E> addBeanProcessor(BeanProcessor<E> processor) {
        beanProcessors.add(processor);
        return this;
    }

    @Override
    public long getStartupTime() {
        if (!isContextReady()) {
            throw new IllegalStateException("Context has not started up");
        }
        return startupTime;
    }

    @Override
    public Date getStartupDate() {
        if (!isContextReady()) {
            throw new IllegalStateException("Context has not started up. You must refresh the context before use.");
        }
        return startupDate;
    }

    protected synchronized void ready() {
        if (isContextReady()) {
            throw new IllegalStateException("Context startup cannot happen more than once");
        }
        for (ContextProcessor<E> contextProcessor : contextProcessors) {
            contextProcessor.postProcessContext(this);
        }
        startupDate = new Date();
        startupTime = System.currentTimeMillis() - startupTime;
    }

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

    @Override
    public E get(String name) throws RegistryException {
        if (!isContextReady()) {
            throw new IllegalStateException("Context not started up");
        }
        E item = super.get(name);
        item = postProcessBeanBeforeAccess(name, item);
        return item;
    }

    private boolean isContextReady() {
        return startupDate != null;
    }

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

    @Override
    public void refresh() {
        markUnprepared();
        ready();
        publishEvent(new ContextRefreshedEvent(this));
    }

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

    @Override
    public Context<E> register(E item) throws RegistryException {
        final String canonicalName = item.getClass().getCanonicalName();
        String name = canonicalName == null ? item.getClass().toString().replaceAll("\\s+", "") : canonicalName;
        name = name.trim() + "#" + (getBeanNames().size() + 1);
        register(name, item);
        return this;
    }

    @Override
    public void addEventListener(EventListener<? extends Event> eventListener) {
        SmartEventListener smartEventListener;
        if (eventListener instanceof SmartEventListener) {
            smartEventListener = (SmartEventListener) eventListener;
        } else {
            //noinspection unchecked
            smartEventListener = new SmartEventListener((EventListener<Event>) eventListener);
        }
        eventListeners.add(smartEventListener);
    }

    @Override
    public <E extends Event> E publishEvent(E event) {
        for (SmartEventListener listener : eventListeners) {
            if (listener.supportsEvent(event)) {
                listener.onEvent(event);
            }
        }
        return event;
    }

    @Override
    public Class<E> getRegistryType() {
        return contextType;
    }

}
