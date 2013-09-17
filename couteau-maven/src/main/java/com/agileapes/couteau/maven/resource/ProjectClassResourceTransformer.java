package com.agileapes.couteau.maven.resource;

import com.agileapes.couteau.basics.api.Transformer;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/17, 14:28)
 */
public class ProjectClassResourceTransformer implements Transformer<ProjectResource, Class<?>> {

    @Override
    public Class<?> map(ProjectResource input) {
        return input.getClassArtifact();
    }

}
