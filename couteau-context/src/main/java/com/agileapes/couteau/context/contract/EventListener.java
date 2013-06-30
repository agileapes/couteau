package com.agileapes.couteau.context.contract;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/29, 13:58)
 */
public interface EventListener<E extends Event> {

    void onEvent(E event);

}
