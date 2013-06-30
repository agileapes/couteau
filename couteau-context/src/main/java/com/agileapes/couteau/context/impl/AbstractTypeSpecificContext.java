package com.agileapes.couteau.context.impl;

import com.agileapes.couteau.context.error.InvalidBeanNameException;
import com.agileapes.couteau.context.error.RegistryException;

import static com.agileapes.couteau.context.contract.OrderedBean.HIGHEST_PRECEDENCE;

/**
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
}
