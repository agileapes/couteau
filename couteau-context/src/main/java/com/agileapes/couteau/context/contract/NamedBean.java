package com.agileapes.couteau.context.contract;

/**
 * Named beans are aware of the name they have been given in any given context
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/3/13, 1:38 PM)
 */
public interface NamedBean {

    /**
     * @param name    the name of the bean in its context
     */
    void setName(String name);

}
