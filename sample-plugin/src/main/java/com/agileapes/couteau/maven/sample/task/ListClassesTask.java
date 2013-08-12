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

package com.agileapes.couteau.maven.sample.task;

import com.agileapes.couteau.maven.resource.ProjectResource;
import com.agileapes.couteau.maven.resource.ProjectResourceType;
import com.agileapes.couteau.maven.sample.SampleExecutor;
import com.agileapes.couteau.maven.task.PluginTask;
import org.apache.maven.plugin.MojoFailureException;

import java.util.Collection;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/4/13, 1:07 PM)
 */
public class ListClassesTask extends PluginTask<SampleExecutor> {

    @Override
    public void execute(SampleExecutor executor) throws MojoFailureException {
        final Collection<ProjectResource> resources = executor.getProjectResources();
        for (ProjectResource resource : resources) {
            if (ProjectResourceType.CLASS.equals(resource.getType())) {
                System.out.println(resource.getClassArtifact().getCanonicalName());
            }
        }
    }

}
