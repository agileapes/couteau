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

package com.agileapes.couteau.maven.resource;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class extends the functionality of {@link org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider}
 * to enable dependent classes to extract classes from the classpath
 *
 * It also allows for exploration of JAR files inside the classpath of the project
 *
 * @author Ramin Farhanian (ramin@farhanian.com)
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0, (7/9/12, 6:02 PM)
 */
@SuppressWarnings("UnusedDeclaration")
public class ClassPathScanningClassProvider extends ClassPathScanningCandidateComponentProvider {

    private String classpathUrlPrefix = DEFAULT_CLASSPATH_URL_PREFIX;
    private static final String DEFAULT_CLASSPATH_URL_PREFIX = "classpath:";
    protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private MetadataReaderFactory metadataReaderFactory;
    private ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

    public ClassPathScanningClassProvider() {
        this(false);
    }

    public ClassPathScanningClassProvider(boolean detectInsideJarFiles) {
        super(false);
        metadataReaderFactory = new CachingMetadataReaderFactory(getResourceLoader());
        if (detectInsideJarFiles) {
            this.classpathUrlPrefix = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;
        }
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.setResourceLoader(new DefaultResourceLoader(classLoader));
    }

    public Set<Class> findCandidateClasses(String basePackage) {
        Set<Class> candidates = new TreeSet<Class>(new Comparator<Class>() {
            @Override
            public int compare(Class dis, Class dat) {
                if (dis==null && dat==null) {
                    return 0;
                } else if (dis == null) {
                    return -1;
                } else if (dat == null) {
                    return 1;
                } else {
                    return dis.getName().compareTo(dat.getName());
                }
            }
        });
        try {
            final String packageSearchPath = classpathUrlPrefix + resolveBasePackage(basePackage) +
                    "/" + DEFAULT_RESOURCE_PATTERN;
            final Resource[] resources = ((ResourcePatternResolver) this.getResourceLoader()).getResources(packageSearchPath);
            if (resources == null) {
                return Collections.emptySet();
            }
            for (Resource resource : resources) {
                if (resource.isReadable() && resource.getURL().getPath() != null) {
                    if (isCandidateComponent(this.metadataReaderFactory.getMetadataReader(resource))) {
                        candidates.add(resolveClass(resource, basePackage));
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("I/O failure during classpath scanning", ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("System failure during classpath scanning. No class were found.", e);
        }
        return candidates;

    }

    private Class resolveClass(Resource resource, String basePackage) throws IOException, ClassNotFoundException {
        final String clazzCleanPath = StringUtils.cleanPath(resource.getURL().getPath());
        final String clazzPathWithoutExtension = StringUtils.stripFilenameExtension(clazzCleanPath);
        final String resourcePackageLikeStr = ClassUtils.convertResourcePathToClassName(clazzPathWithoutExtension);
        final String className = resourcePackageLikeStr.substring(resourcePackageLikeStr.lastIndexOf(basePackage));
        return ClassUtils.forName(className, classLoader);
    }

}
