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

package com.agileapes.couteau.lang.compiler.impl;

import com.agileapes.couteau.reflection.cp.MappedClassLoader;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This is an extension to {@link javax.tools.JavaFileManager} which enables the framework
 * to create passive links between Java byte code and the actual files written
 * for output.
 *
 * The class loader is used by the compiler to register a file inside this file
 * manager with that class loader. The class loader internally used here is the
 * {@link com.agileapes.couteau.reflection.cp.MappedClassLoader} which can be later used to <em>read</em> the byte
 * code information as it was produced by the JIT compiler.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/16/12)
 */
public class SingleLocationClassFileManager<M extends JavaFileManager> extends ForwardingJavaFileManager<M> {


    private final MappedClassLoader mappedClassLoader;
    private final Map<String, JavaClassObject> objectMap = new HashMap<String, JavaClassObject>();

    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param classLoader the class loader for the compilation
     * @param fileManager delegate to this file manager
     */
    public SingleLocationClassFileManager(ClassLoader classLoader, M fileManager) {
        super(fileManager);
        mappedClassLoader = new MappedClassLoader(classLoader);
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return mappedClassLoader;
    }

    /**
     * This method will return the class object file associated with the fully
     * qualified name given here.
     * @param name    the fully qualified name leading to this class
     * @return the class file object
     */
    public JavaClassObject getClassObject(String name) throws ClassNotFoundException {
        if (!objectMap.containsKey(name)) {
            throw new ClassNotFoundException(name);
        }
        return objectMap.get(name);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        final JavaClassObject object = new JavaClassObject(className, kind);
        objectMap.put(className, object);
        return object;
    }

    public Map<String, JavaClassObject> getObjectMap() {
        return objectMap;
    }
}
