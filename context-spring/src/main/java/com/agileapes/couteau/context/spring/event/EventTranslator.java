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
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/29/13, 4:15 PM)
 */
public class EventTranslator implements EventListener<Event>, ApplicationContextAware {

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
        targetScheme.fillIn(event, applicationEvent);
    }

}
