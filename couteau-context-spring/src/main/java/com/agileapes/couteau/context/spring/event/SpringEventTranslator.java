/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agileapes.couteau.context.spring.event;

import com.agileapes.couteau.context.contract.Event;
import com.agileapes.couteau.context.contract.EventListener;
import com.agileapes.couteau.context.impl.OrderedBeanComparator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class, when included in a Spring application context, will enable the translation
 * of events going through Couteau contexts into events that can be listened to by Spring
 * ApplicationListener instances.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/29/13, 4:15 PM)
 */
public class SpringEventTranslator implements EventListener<Event>, ApplicationContextAware {

    private final List<TranslationScheme> schemes = new ArrayList<TranslationScheme>();
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        schemes.addAll(applicationContext.getBeansOfType(TranslationScheme.class, false, true).values());
        Collections.sort(schemes, new OrderedBeanComparator());
        Collections.sort(schemes, new Comparator<TranslationScheme>() {
            @Override
            public int compare(TranslationScheme o1, TranslationScheme o2) {
                final Integer first = o1 instanceof Ordered ? ((Ordered) o1).getOrder() : 0;
                final Integer second = o2 instanceof Ordered ? ((Ordered) o2).getOrder() : 0;
                return first.compareTo(second);
            }
        });
    }

    @Override
    public void onEvent(Event event) {
        ApplicationEvent applicationEvent = null;
        TranslationScheme targetScheme = null;
        for (TranslationScheme scheme : schemes) {
            if (scheme.handles(event)) {
                try {
                    targetScheme = scheme;
                    applicationEvent = scheme.translate(event);
                    break;
                } catch (Exception ignored) {}
            }
        }
        if (applicationEvent == null) {
            return;
        }
        applicationContext.publishEvent(applicationEvent);
        try {
            targetScheme.fillIn(event, applicationEvent);
        } catch (com.agileapes.couteau.context.spring.error.EventTranslationException e) {
            e.printStackTrace();
        }
    }

}
