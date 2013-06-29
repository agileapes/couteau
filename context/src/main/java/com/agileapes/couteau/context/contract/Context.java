package com.agileapes.couteau.context.contract;

import com.agileapes.couteau.context.error.RegistryException;

import java.util.Date;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:09 PM)
 */
public interface Context<E> extends Registry<E>, EventPublisher {

    void addContextProcessor(ContextProcessor<E> processor);

    void addBeanProcessor(BeanProcessor<E> processor);

    long getStartupTime();

    Date getStartupDate();

    <T extends E> Map<String, T> getBeansOfType(Class<T> type) throws RegistryException;

    void register(E item) throws RegistryException;

    void addEventListener(EventListener<? extends Event> eventListener);

}
