package com.agileapes.couteau.context.impl;

import com.agileapes.couteau.context.contract.OrderedBean;

import java.util.Comparator;

/**
 * This is a comparator that is designed to assist in ordering beans. Beans that do not implement
 * {@link OrderedBean} are assigned a {@link OrderedBean#NEUTRAL_PRECEDENCE}. Beans that do are
 * taken to have the precedence they declare.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:21 PM)
 */
public class OrderedBeanComparator implements Comparator<Object> {

    @Override
    public int compare(Object o1, Object o2) {
        Integer first = o1 instanceof OrderedBean ? ((OrderedBean) o1).getOrder() : OrderedBean.NEUTRAL_PRECEDENCE;
        Integer second = o2 instanceof OrderedBean ? ((OrderedBean) o2).getOrder() : OrderedBean.NEUTRAL_PRECEDENCE;
        return first.compareTo(second);
    }

}
