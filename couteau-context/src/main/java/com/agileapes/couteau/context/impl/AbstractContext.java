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
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:19 PM)
 */
public abstract class AbstractContext<E> extends AbstractRegistry<E> implements ReconfigurableContext<E> {

    private final List<BeanProcessor<E>> beanProcessors = new CopyOnWriteArrayList<BeanProcessor<E>>();
    private final List<ContextProcessor<E>> contextProcessors = new CopyOnWriteArrayList<ContextProcessor<E>>();
    private final List<SmartEventListener> eventListeners = new CopyOnWriteArrayList<SmartEventListener>();
    private Date startupDate = null;
    private long startupTime = System.currentTimeMillis();
    private final Class<E> contextType;

    public AbstractContext() {
        //noinspection unchecked
        contextType = (Class<E>) ClassUtils.resolveTypeArgument(getClass(), AbstractContext.class);
        addBeanProcessor(new BeanProcessorAdapter<E>() {
            @Override
            public E postProcessBeforeRegistration(E bean, String name) throws RegistryException {
                if (bean instanceof ContextAware) {
                    final ContextAware contextAware = (ContextAware) bean;
                    final Class<?> awareType = ClassUtils.resolveTypeArgument(contextAware.getClass(), ContextAware.class);
                    final Class<?> currentType = getContextType();
                    if ((awareType == null && currentType.equals(Object.class)) || (awareType != null && awareType.isAssignableFrom(currentType))) {
                        //noinspection unchecked
                        contextAware.setContext(AbstractContext.this);
                    }
                }
                return bean;
            }
        });
        addBeanProcessor(new BeanProcessorAdapter<E>() {
            @Override
            public E postProcessBeforeRegistration(E bean, String name) throws RegistryException {
                if (bean instanceof EventListener<?>) {
                    addEventListener((EventListener<?>) bean);
                }
                return bean;
            }
        });
        publishEvent(new ContextStartupEvent(this));
    }

    public Context<E> addContextProcessor(ContextProcessor<E> processor) {
        contextProcessors.add(processor);
        startupDate = null;
        return this;
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
        startupDate = null;
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
    public Context<E> addEventListener(EventListener<? extends Event> eventListener) {
        SmartEventListener smartEventListener;
        if (eventListener instanceof SmartEventListener) {
            smartEventListener = (SmartEventListener) eventListener;
        } else {
            //noinspection unchecked
            smartEventListener = new SmartEventListener((EventListener<Event>) eventListener);
        }
        eventListeners.add(smartEventListener);
        return this;
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
    public Class<E> getContextType() {
        return contextType;
    }

}
