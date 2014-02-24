package com.agileapes.couteau.graph.query.filters;

import com.agileapes.couteau.graph.node.Node;
import com.agileapes.couteau.graph.node.NodeFilter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/2/24 AD, 19:50)
 */
public class OriginNodeFilter<N extends Node> implements OriginNodeAware<N>, NodeFilter<N> {

    private N origin;

    @Override
    public boolean accepts(N item) {
        return origin.equals(item);
    }

    @Override
    public void setOrigin(N origin) {
        this.origin = origin;
    }
}
