package com.agileapes.couteau.context.spring.error;

/**
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
