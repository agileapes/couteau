package com.agileapes.couteau.context.value;

/**
 * This is a convenience interface designed to let underlying systems to inject the value
 * reader context into beans without exposing it as a context, to minimize access and maximize
 * object security.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/3/13, 11:43 AM)
 */
public interface ValueReaderAware {

    /**
     * Method for injecting value readers into beans
     * @param valueReader    the value reader
     */
    void setValueReader(ValueReader valueReader);

}
