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

package com.agileapes.couteau.maven.sample;

import com.agileapes.couteau.maven.mojo.AbstractSpringPluginExecutor;
import org.apache.maven.project.MavenProject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/4/13, 12:59 PM)
 * @goal execute
 * @phase process-classes
 * @execute phase="process-classes"
 * @requiresDependencyResolution process-classes
 */
public class SampleExecutor extends AbstractSpringPluginExecutor {

    /**
     * @parameter expression='${project}'
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter
     * @required
     */
    private String[] packages;

    @Override
    public MavenProject getProject() {
        return project;
    }

    @Override
    public Set<String> getScanPackages() {
        return new HashSet<String>(Arrays.asList(packages));
    }

    @Override
    protected ApplicationContext loadApplicationContext() {
        return new ClassPathXmlApplicationContext("plugin.xml");
    }

}
