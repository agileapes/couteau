/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
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

package com.mmnaseri.couteau.security.impl;

import com.mmnaseri.couteau.security.AccessPolicy;
import com.mmnaseri.couteau.security.Actor;
import com.mmnaseri.couteau.security.Decision;
import com.mmnaseri.couteau.basics.api.Filter;
import com.mmnaseri.couteau.security.Subject;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/8/30 AD, 10:28)
 */
public class ImmutableAccessPolicy implements AccessPolicy {

    private final String name;
    private final Decision decision;
    private final Filter<Actor> actorFilter;
    private final Filter<Subject> subjectFilter;

    public ImmutableAccessPolicy(String name, Decision decision, Filter<Actor> actorFilter, Filter<Subject> subjectFilter) {
        this.name = name;
        this.decision = decision;
        this.actorFilter = actorFilter;
        this.subjectFilter = subjectFilter;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Filter<Actor> getActorFilter() {
        return actorFilter;
    }

    @Override
    public Filter<Subject> getSubjectFilter() {
        return subjectFilter;
    }

    @Override
    public Decision getDecision() {
        return decision;
    }

}
