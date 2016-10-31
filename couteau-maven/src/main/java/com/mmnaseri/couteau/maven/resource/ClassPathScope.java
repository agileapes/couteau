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

package com.mmnaseri.couteau.maven.resource;

/**
 * These are the possible scopes in the project's classpath.
 *
 * For more information see <a href="http://docs.codehaus.org/display/MAVENUSER/Dependency+Scopes">Maven's
 * documentation at Codehaus</a>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0, (7/11/12, 3:03 PM)
 */
@SuppressWarnings("UnusedDeclaration")
public enum ClassPathScope {

    /**
     * Resources in the compile scope are necessary for the successful compilation of the project
     */
    COMPILE("compile"),
    /**
     * Provided resources are resources that are required by the project but will be provided by an
     * outside container and as such should not be bundled with the resources of the project when
     * packaging
     */
    PROVIDED("provided"),
    /**
     * Runtime resources are necessary for the running of the application, while not necessary for
     * the compilation
     */
    RUNTIME("runtime"),
    /**
     * System artifacts are expected to be provided by the target platform
     */
    SYSTEM("system"),
    /**
     * Artifacts in the test scope will be necessary for running tests but not for compiling
     * the rest of the project
     */
    TEST("test");

    private String name;

    private ClassPathScope(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
