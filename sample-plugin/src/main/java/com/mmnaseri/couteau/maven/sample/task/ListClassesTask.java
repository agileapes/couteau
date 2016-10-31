/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mmnaseri.couteau.maven.sample.task;

import com.mmnaseri.couteau.maven.resource.ProjectResource;
import com.mmnaseri.couteau.maven.resource.ProjectResourceType;
import com.mmnaseri.couteau.maven.sample.SampleExecutor;
import com.mmnaseri.couteau.maven.task.PluginTask;
import org.apache.maven.plugin.MojoFailureException;

import java.util.Collection;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
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
