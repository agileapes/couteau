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

package com.mmnaseri.couteau.maven.resource.impl;

import com.mmnaseri.couteau.basics.api.Filter;
import com.mmnaseri.couteau.maven.resource.ClassPathScope;
import org.apache.maven.artifact.Artifact;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (8/3/13, 1:19 PM)
 */
public class ClassPathScopeArtifactFilter implements Filter<Artifact> {

    private Set<String> scopes = new HashSet<String>();

    public ClassPathScopeArtifactFilter(Collection<ClassPathScope> scopes) {
        for (ClassPathScope scope : scopes) {
            this.scopes.add(scope.toString());
        }
    }

    public ClassPathScopeArtifactFilter(ClassPathScope... scopes) {
        this(Arrays.asList(scopes));
    }

    @Override
    public boolean accepts(Artifact item) {
        return scopes.contains(item.getScope());
    }

}
