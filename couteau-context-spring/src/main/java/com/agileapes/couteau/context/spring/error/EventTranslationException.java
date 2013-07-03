package com.agileapes.couteau.context.spring.error;

/**
 * This exception indicates an error in the process of translating the event
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/30, 10:15)
 */
public class EventTranslationException extends Exception {

    public EventTranslationException(String message) {
        super(message);
    }

    public EventTranslationException(String message, Throwable cause) {
        super(message, cause);
    }

}
