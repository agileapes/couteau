package com.agileapes.couteau.xml.query.filters;

import com.agileapes.couteau.graph.node.ConfigurableNodeFilter;
import com.agileapes.couteau.xml.node.XmlNode;

import static com.agileapes.couteau.basics.collections.CollectionWrapper.with;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/2/24 AD, 19:27)
 */
public class NodeNamespaceFilter<N extends XmlNode> implements ConfigurableNodeFilter<N> {

    private String namespace;

    @Override
    public void setAttribute(String name, String value) {
        if (with("0", "namespace", "ns").has(name)) {
            namespace = value;
        }
    }

    @Override
    public boolean accepts(N item) {
        return item.getNamespace() != null && item.getNamespace().matches(namespace);
    }

}
