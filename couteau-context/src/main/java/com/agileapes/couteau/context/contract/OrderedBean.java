package com.agileapes.couteau.context.contract;

/**
 * Ordered beans allow you to prioritize over beans.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:09 PM)
 */
public interface OrderedBean {

    /**
     * The highest precedence. Makes the bean have higher priority than any other bean.
     */
    public static final int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * Gives the bean the least importance
     */
    public static final int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * Makes the bean of no special priority
     */
    public static final int NEUTRAL_PRECEDENCE = 0;

    /**
     * @return the order of the bean as deemed by the bean itself
     */
    int getOrder();

}
