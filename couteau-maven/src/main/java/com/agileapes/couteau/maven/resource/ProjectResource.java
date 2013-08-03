/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.couteau.maven.resource;

import org.springframework.core.io.Resource;

/**
 * The project resource is a generic wrapper for all project artifacts available to plugin tasks
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (5/12/13, 12:07 PM)
 */
public class ProjectResource {

    private final ProjectResourceType type;
    private final Object artifact;

    public ProjectResource(Object artifact) {
        this.artifact = artifact;
        this.type = artifact instanceof Resource ? ProjectResourceType.RESOURCE : ProjectResourceType.CLASS;
    }

    public ProjectResourceType getType() {
        return type;
    }

    public Object getArtifact() {
        return artifact;
    }

    public Class<?> getClassArtifact() {
        return (Class<?>) artifact;
    }

    public Resource getResourceArtifact() {
        return (Resource) artifact;
    }

    @Override
    public String toString() {
        return "@" + getType() + ":" + getArtifact();
    }
}
