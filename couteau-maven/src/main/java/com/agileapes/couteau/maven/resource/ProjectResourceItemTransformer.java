package com.agileapes.couteau.maven.resource;

import com.agileapes.couteau.basics.api.Transformer;
import org.springframework.core.io.Resource;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/17, 14:29)
 */
public class ProjectResourceItemTransformer implements Transformer<ProjectResource, Resource> {

    @Override
    public Resource map(ProjectResource input) {
        return input.getResourceArtifact();
    }

}
