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
