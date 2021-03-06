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

package com.mmnaseri.couteau.freemarker.model;

import com.mmnaseri.couteau.basics.api.Transformer;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BooleanModel;
import freemarker.template.*;

import java.util.HashMap;
import java.util.Map;

import static com.mmnaseri.couteau.basics.collections.CollectionWrapper.with;

/**
 * This class will model a class data model.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (7/15/13, 6:46 PM)
 */
public class SimpleClassModel implements TemplateHashModelEx {

    private final Map<String, TemplateModel> values = new HashMap<String, TemplateModel>();

    public SimpleClassModel(Class<?> target) {
        values.put("localClass", new BooleanModel(target.isLocalClass(), BeansWrapper.getDefaultInstance()));
        values.put("anonymousClass", new BooleanModel(target.isAnonymousClass(), BeansWrapper.getDefaultInstance()));
        values.put("packageName", new SimpleScalar(target.getCanonicalName().contains(".") ? target.getCanonicalName().substring(0, target.getCanonicalName().lastIndexOf(".")) : ""));
        values.put("synthetic", new BooleanModel(target.isSynthetic(), BeansWrapper.getDefaultInstance()));
        values.put("annotation", new BooleanModel(target.isAnnotation(), BeansWrapper.getDefaultInstance()));
        values.put("name", new SimpleScalar(target.getName()));
        values.put("memberClass", new BooleanModel(target.isMemberClass(), BeansWrapper.getDefaultInstance()));
        values.put("modifiers", new SimpleNumber(target.getModifiers()));
        values.put("primitive", new BooleanModel(target.isPrimitive(), BeansWrapper.getDefaultInstance()));
        values.put("simpleName", new SimpleScalar(target.getSimpleName()));
        values.put("enum", new BooleanModel(target.isEnum(), BeansWrapper.getDefaultInstance()));
        values.put("interface", new BooleanModel(target.isInterface(), BeansWrapper.getDefaultInstance()));
        values.put("canonicalName", new SimpleScalar(target.getCanonicalName()));
        values.put("array", new BooleanModel(target.isArray(), BeansWrapper.getDefaultInstance()));
    }

    @Override
    public int size() throws TemplateModelException {
        return values.size();
    }

    @Override
    public TemplateCollectionModel keys() throws TemplateModelException {
        return new SimpleCollection(with(values.keySet()).transform(new Transformer<String, SimpleScalar>() {
            @Override
            public SimpleScalar map(String input) {
                return new SimpleScalar(input);
            }
        }).list());
    }

    @Override
    public TemplateCollectionModel values() throws TemplateModelException {
        return new SimpleCollection(values.values());
    }

    @Override
    public TemplateModel get(String key) throws TemplateModelException {
        return values.get(key);
    }

    @Override
    public boolean isEmpty() throws TemplateModelException {
        return values.isEmpty();
    }

}
