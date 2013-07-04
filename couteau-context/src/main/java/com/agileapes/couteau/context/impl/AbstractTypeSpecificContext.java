package com.agileapes.couteau.context.impl;

import com.agileapes.couteau.context.contract.Context;
import com.agileapes.couteau.context.error.InvalidBeanNameException;
import com.agileapes.couteau.context.error.RegistryException;

import static com.agileapes.couteau.context.contract.OrderedBean.HIGHEST_PRECEDENCE;

/**
 * This is a thread-safe context that requires items to be named after the canonical names
 * of their classes. This ensures that singleton objects within the context are unique by
 * their types as well.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:35 PM)
 */
public abstract class AbstractTypeSpecificContext<E> extends AbstractThreadSafeContext<E> {

    public AbstractTypeSpecificContext() {
        addBeanProcessor(new BeanProcessorAdapter<E>(HIGHEST_PRECEDENCE) {
            @Override
            public E postProcessBeforeRegistration(E bean, String name) throws RegistryException {
                if (name == null || !name.equals(bean.getClass().getCanonicalName())) {
                    throw new InvalidBeanNameException("Expected bean to be named " + bean.getClass().getCanonicalName() + " but was named " + name);
                }
                return bean;
            }
        });
    }

    @Override
    public Context<E> register(E item) throws RegistryException {
        register(item.getClass().getCanonicalName(), item);
        return this;
    }

}
