package com.agileapes.couteau.context.impl;

import com.agileapes.couteau.context.contract.BeanProcessor;
import com.agileapes.couteau.context.contract.ContextProcessor;
import com.agileapes.couteau.context.contract.ReconfigurableContext;
import com.agileapes.couteau.context.error.RegistryException;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:19 PM)
 */
public abstract class AbstractContext<E> extends AbstractRegistry<E> implements ReconfigurableContext<E> {

    private final List<BeanProcessor<E>> beanProcessors = new CopyOnWriteArrayList<BeanProcessor<E>>();
    private final List<ContextProcessor<E>> contextProcessors = new CopyOnWriteArrayList<ContextProcessor<E>>();
    private Date startupDate = null;
    private long startupTime = System.currentTimeMillis();

    public void addContextProcessor(ContextProcessor<E> processor) {
        contextProcessors.add(processor);
    }

    @Override
    public void addBeanProcessor(BeanProcessor<E> processor) {
        beanProcessors.add(processor);
        Collections.sort(beanProcessors, new OrderedBeanComparator());
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
            throw new IllegalStateException("Context has not started up");
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
        for (BeanProcessor<E> processor : beanProcessors) {
            item = processor.postProcessBeforeRegistration(item, name);
        }
        super.register(name, item);
    }

    @Override
    public E get(String name) throws RegistryException {
        if (!isContextReady()) {
            throw new IllegalStateException("Context not started up");
        }
        E item = super.get(name);
        item = postProcessBean(name, item);
        return item;
    }

    private boolean isContextReady() {
        return startupDate != null;
    }

    private E postProcessBean(String name, E item) {
        for (BeanProcessor<E> processor : beanProcessors) {
            try {
                item = processor.postProcessBeforeAccess(item, name);
            } catch (RegistryException e) {
                e.printStackTrace();
            }
            if (item == null) {
                throw new NullPointerException("Item cannot be nullified by a post processor");
            }
        }
        return item;
    }

    @Override
    public void refresh() {
        startupDate = null;
        ready();
    }

    @Override
    public Map<String, ? extends E> getBeansOfType(Class<? extends E> type) throws RegistryException {
        final Collection<String> beanNames = getBeanNames();
        final HashMap<String, E> map = new HashMap<String, E>();
        for (String beanName : beanNames) {
            final E bean = get(beanName);
            if (type.isInstance(bean)) {
                map.put(beanName, bean);
            }
        }
        return map;
    }

}
