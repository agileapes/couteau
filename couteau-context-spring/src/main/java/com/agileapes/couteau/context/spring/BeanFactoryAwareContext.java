package com.agileapes.couteau.context.spring;

import org.springframework.beans.factory.BeanFactory;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/29/13, 4:04 PM)
 */
public interface BeanFactoryAwareContext {

    void setBeanFactory(BeanFactory beanFactory);

}
