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

package com.mmnaseri.couteau.lang.model;

import com.mmnaseri.couteau.freemarker.api.Template;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (5/22/13, 2:21 PM)
 */
@Template("ftl/javaClass.ftl")
public class JavaClassModel extends JavaPojoModel {

    private final Set<MethodModel> methods = new HashSet<MethodModel>();
    private final Map<String, Boolean> immutables = new HashMap<String, Boolean>();

    public JavaClassModel(String qualifiedName) {
        super(qualifiedName);
    }

    public MethodModel addMethod(String name, String returnType, ParameterModel ... parameters) {
        final MethodModel model = new MethodModel(name, returnType, parameters);
        methods.add(model);
        return model;
    }

    public MethodModel addVoidMethod(String name, ParameterModel ... parameters) {
        return addMethod(name, null, parameters);
    }

    public Set<MethodModel> getMethods() {
        return methods;
    }

    public Map<String, Boolean> getImmutables() {
        return immutables;
    }

    @Override
    public void addProperty(String property, String type) {
        super.addProperty(property, type);
        immutables.put(property, false);
    }

    public void addImmutableProperty(String type, String property) {
        super.addProperty(property, type);
        immutables.put(property, true);
    }

}
