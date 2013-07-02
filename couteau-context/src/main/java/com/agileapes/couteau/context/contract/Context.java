package com.agileapes.couteau.context.contract;

import com.agileapes.couteau.context.error.RegistryException;

import java.util.Date;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:09 PM)
 */
public interface Context<E> extends Registry<E>, EventPublisher {

    Context<E> addContextProcessor(ContextProcessor<E> processor);

    Context<E> addBeanProcessor(BeanProcessor<E> processor);

    long getStartupTime();

    Date getStartupDate();

    Context<E> register(E item) throws RegistryException;

    Context<E> addEventListener(EventListener<? extends Event> eventListener);

    <T extends E> Map<String, T> getBeansOfType(Class<T> type) throws RegistryException;

}
