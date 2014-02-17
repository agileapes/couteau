package com.agileapes.couteau.graph.binding.impl;

import com.agileapes.couteau.graph.binding.ObjectReader;
import com.agileapes.couteau.graph.node.Node;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/2/17 AD, 18:24)
 */
public class DefaultObjectReader<N extends Node> implements ObjectReader<N> {

    @Override
    public <E> E read(Class<E> type, N node) {
        return null;
    }
}
