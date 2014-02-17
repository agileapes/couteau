package com.agileapes.couteau.xml.query.filters;

import com.agileapes.couteau.graph.node.ConfigurableNodeFilter;
import com.agileapes.couteau.xml.node.XmlNode;

import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/2/17 AD, 17:38)
 */
public class NodeValueFilter implements ConfigurableNodeFilter<XmlNode> {

    private Pattern pattern;

    @Override
    public void setAttribute(String name, String value) {
        if ("pattern".equals(name) || "0".equals(name)) {
            pattern = Pattern.compile(value, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        }
    }

    @Override
    public boolean accepts(XmlNode item) {
        return item.getNodeValue() != null && pattern.matcher(item.getNodeValue()).matches();
    }

}
