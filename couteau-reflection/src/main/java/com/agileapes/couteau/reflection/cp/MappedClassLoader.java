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

package com.agileapes.couteau.reflection.cp;

import javax.tools.JavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.SecureClassLoader;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a class-loader that holds the class information in-memory. As it extends
 * {@link java.security.SecureClassLoader} it provides all of the functionalities of a normal class-
 * loader with the added benefit of being able to register new classes with it using
 * the {@link #register(String, byte[])} method.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/16/12)
 */
public class MappedClassLoader extends SecureClassLoader {

    private Map<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>();
    private Map<String, byte[]> bytes = new ConcurrentHashMap<String, byte[]>();

    public MappedClassLoader() {
    }

    public MappedClassLoader(ClassLoader parent) {
        super(parent);
    }

    public MappedClassLoader(ClassLoader parent, MappedClassLoader helper) {
        super(parent);
        for (Map.Entry<String, byte[]> entry : helper.bytes.entrySet()) {
            register(entry.getKey(), entry.getValue());
        }
    }

    /**
     * This method will take as input Java&trade; byte code and keep it in
     * as class definition.
     * @param name     fully qualified name for this class
     * @param bytes    stream of Java byte code
     * @see #getBytes(String)
     */
    public void register(String name, byte[] bytes) {
        classes.put(name, super.defineClass(name, bytes, 0, bytes.length));
        this.bytes.put(name, bytes);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        if (!classes.containsKey(name)) {
            if (getParent() != null) {
                return getParent().loadClass(name);
            }
            throw new ClassNotFoundException("Class not found: " + name);
        }
        return classes.get(name);
    }

    /**
     * This method will return the byte code associated with the given
     * class name. This is specially handy if you ever need to persist
     * the in-memory registered class.
     * @param name    the fully qualified name leading to the class
     * @return byte code associated with the name
     * @throws ClassNotFoundException if no such class has been registered.
     */
    public byte[] getBytes(String name) throws ClassNotFoundException {
        if (!bytes.containsKey(name)) {
            throw new ClassNotFoundException("Class not found: " + name);
        }
        return bytes.get(name);
    }

    public Collection<String> getClassNames() {
        return classes.keySet();
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        final byte[] bytes;
        String resourceName = name;
        if (name.endsWith(JavaFileObject.Kind.CLASS.extension)) {
            name = name.substring(0, name.length() - JavaFileObject.Kind.CLASS.extension.length());
            name = name.replace("/", ".");
            name = name.replace("\\", ".");
            if (name.startsWith("/") || name.startsWith("\\")) {
                name = name.substring(1);
            }
        }
        try {
            bytes = getBytes(name);
        } catch (ClassNotFoundException e) {
            if (getParent() != null) {
                return getParent().getResourceAsStream(resourceName);
            }
            return null;
        }
        return new ByteArrayInputStream(bytes);
    }

}
