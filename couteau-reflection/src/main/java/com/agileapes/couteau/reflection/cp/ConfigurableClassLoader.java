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

package com.agileapes.couteau.reflection.cp;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.SecureClassLoader;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This extension to SecureClassLoader allows for run-time modification of classpath elements, in such a way that new
 * ways to find classes can be introduced during the execution of the plugin
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/4/13, 6:37 PM)
 */
public class ConfigurableClassLoader extends SecureClassLoader {

    private final List<ClassLoader> classLoaders = new CopyOnWriteArrayList<ClassLoader>();
    private final Map<String, Class<?>> map = new HashMap<String, Class<?>>();

    public ConfigurableClassLoader(ClassLoader parent) {
        super(parent);
    }

    public ConfigurableClassLoader() {
        super();
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (map.containsKey(name)) {
            return map.get(name);
        }
        for (ClassLoader classLoader : classLoaders) {
            try {
                return classLoader.loadClass(name);
            } catch (ClassNotFoundException ignored) {
            }
        }
        return super.loadClass(name);
    }

    public void addClassLoader(ClassLoader classLoader) {
        classLoaders.add(classLoader);
    }

    public void addClass(Class<?> loadedClass) {
        map.put(loadedClass.getCanonicalName(), loadedClass);
    }

    private URL[] getClassLoaderUrls(ClassLoader classLoader) {
        if (classLoader != null && classLoader instanceof URLClassLoader) {
            return ((URLClassLoader) classLoader).getURLs();
        }
        return new URL[0];
    }

    public URL[] getUrls() {
        final List<URL> list = new ArrayList<URL>();
        Collections.addAll(list, getClassLoaderUrls(getParent()));
        for (ClassLoader loader : classLoaders) {
            Collections.addAll(list, getClassLoaderUrls(loader));
        }
        return list.toArray(new URL[list.size()]);
    }

}
