package com.agileapes.couteau.context.spring.event;

import com.agileapes.couteau.context.contract.Event;
import org.springframework.context.ApplicationEvent;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/29/13, 4:16 PM)
 */
public interface TranslationScheme {

    boolean handles(Event event);

    ApplicationEvent translate(Event originalEvent);

    void fillIn(Event originalEvent, ApplicationEvent translated);

}
