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

import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.lang.compiler.impl.DefaultDynamicClassCompiler;
import com.agileapes.couteau.lang.compiler.impl.SimpleJavaSourceCompiler;
import com.agileapes.couteau.lang.error.CompileException;
import com.agileapes.couteau.maven.resource.ConfigurableClassLoader;
import com.agileapes.couteau.maven.sample.SampleExecutor;
import com.agileapes.couteau.maven.sample.code.GeneratedCodeRepository;
import com.agileapes.couteau.maven.task.PluginTask;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoFailureException;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.agileapes.couteau.basics.collections.CollectionWrapper.with;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/4/13, 7:06 PM)
 */
public class CompileCodeTask extends PluginTask<SampleExecutor> {

    private final GeneratedCodeRepository repository;

    public CompileCodeTask(GeneratedCodeRepository repository) {
        this.repository = repository;
    }

    /**
     * This method will determine the classpath argument passed to the JavaC compiler used by this
     * implementation
     * @return the classpath
     * @throws org.apache.maven.artifact.DependencyResolutionRequiredException if the resolution of classpath requires a prior
     * resolution of dependencies for the target project
     */
    private String getClassPath(SampleExecutor executor) throws DependencyResolutionRequiredException {
        //Adding compile time dependencies for the project
        final List elements = executor.getProject().getCompileClasspathElements();
        final Set<File> classPathElements = new HashSet<File>();
        //noinspection unchecked
        classPathElements.addAll(elements);
        //Adding runtime dependencies available to the target project
        final ClassLoader loader = executor.getProjectClassLoader();
        URL[] urls = new URL[0];
        if (loader instanceof URLClassLoader) {
            urls = ((URLClassLoader) loader).getURLs();
        } else if (loader instanceof ConfigurableClassLoader) {
            urls = ((ConfigurableClassLoader) loader).getUrls();
        }
        for (URL url : urls) {
            try {
                final File file = new File(url.toURI());
                if (file.exists()) {
                    classPathElements.add(file);
                }
            } catch (Throwable ignored) {
            }
        }
        if (loader instanceof ConfigurableClassLoader) {
            final ConfigurableClassLoader classLoader = (ConfigurableClassLoader) loader;

        }
        //Adding dependency artifacts for the target project
        for (Object dependencyArtifact : executor.getProject().getDependencyArtifacts()) {
            if (!(dependencyArtifact instanceof DefaultArtifact)) {
                continue;
            }
            DefaultArtifact artifact = (DefaultArtifact) dependencyArtifact;
            if (artifact.getFile() != null) {
                classPathElements.add(artifact.getFile());
            }
        }
        return StringUtils.collectionToDelimitedString(classPathElements, File.pathSeparator);
    }

    @Override
    public void execute(final SampleExecutor executor) throws MojoFailureException {
        final DefaultDynamicClassCompiler compiler = new DefaultDynamicClassCompiler(executor.getProjectClassLoader());
        try {
            compiler.setOption(SimpleJavaSourceCompiler.Option.CLASSPATH, getClassPath(executor));
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoFailureException("Classpath resolution failed", e);
        }
        with(repository.getClassNames()).each(new Processor<String>() {
            @Override
            public void process(String className) {
                final Class<?> compiledClass;
                try {
                    compiledClass = compiler.compile(className, new StringReader(repository.getSourceCode(className)));
                } catch (CompileException e) {
                    throw new Error("Compilation error", e);
                }
                System.out.println("Compiled: " + compiledClass.getCanonicalName());
                ((ConfigurableClassLoader) executor.getProjectClassLoader()).addClass(compiledClass);
            }
        });
        try {
            final Class<?> aClass = executor.getProjectClassLoader().loadClass("com.agileapes.couteau.graph.node.impl.DefaultSearchableNodeAccessor");
            System.out.println("aClass = " + aClass);
        } catch (ClassNotFoundException e) {
        }
    }

}
