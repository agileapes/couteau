package com.agileapes.couteau.context.contract;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:09 PM)
 */
public interface OrderedBean {

    public static final int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
    public static final int LOWEST_PRECEDENCE = Integer.MAX_VALUE;
    public static final int NEUTRAL_PRECEDENCE = 0;

    int getOrder();

}
