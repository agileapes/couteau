package com.agileapes.couteau.graph.node;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/2/17 AD, 19:06)
 */
public interface NodeFilterRepository {

    <N extends Node> NodeFilter<N> getFilter(String filterName, N node);

    void register(String filterName, NodeFilter<?> nodeFilter);

}
