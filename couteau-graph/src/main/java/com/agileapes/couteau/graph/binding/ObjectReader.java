package com.agileapes.couteau.graph.binding;

import com.agileapes.couteau.graph.error.GraphBindingError;
import com.agileapes.couteau.graph.node.Node;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/2/17 AD, 18:24)
 */
public interface ObjectReader<N extends Node> {

    <E> E read(Class<E> type, N node) throws GraphBindingError;

}
