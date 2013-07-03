package com.agileapes.couteau.context.spring;

import org.springframework.beans.factory.BeanFactory;

/**
 * This interface will allow contexts that are not essentially a part of the Spring context being
 * used to gain access to its bean factory.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/29/13, 4:04 PM)
 */
public interface BeanFactoryAwareContext {

    /**
     * Method used to inject dependency
     * @param beanFactory    the bean factory
     */
    void setBeanFactory(BeanFactory beanFactory);

}
