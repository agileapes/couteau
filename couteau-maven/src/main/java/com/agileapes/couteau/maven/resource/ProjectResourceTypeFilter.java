package com.agileapes.couteau.maven.resource;

import com.agileapes.couteau.basics.api.Filter;

/**
 * Filters resources based on their type
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/17, 14:27)
 */
public class ProjectResourceTypeFilter implements Filter<ProjectResource> {

    private final ProjectResourceType type;

    public ProjectResourceTypeFilter(ProjectResourceType type) {
        this.type = type;
    }

    @Override
    public boolean accepts(ProjectResource item) {
        return item.getType().equals(type);
    }
}
