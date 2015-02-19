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

package com.agileapes.couteau.freemarker.model;

import com.agileapes.couteau.freemarker.utils.FreemarkerUtils;
import freemarker.template.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/4/13, 4:49 PM)
 */
public class SimpleMapModel implements TemplateHashModelEx {

    private final Map<String, TemplateModel> map = new HashMap<String, TemplateModel>();

    public SimpleMapModel(Map<?,?> map) {
        putAll(map);
    }

    public SimpleMapModel() {
    }

    @Override
    public int size() throws TemplateModelException {
        return map.size();
    }

    @Override
    public TemplateCollectionModel keys() throws TemplateModelException {
        return new SimpleCollection(map.keySet());
    }

    @Override
    public TemplateCollectionModel values() throws TemplateModelException {
        return new SimpleCollection(map.values());
    }

    @Override
    public TemplateModel get(String key) throws TemplateModelException {
        return map.get(key);
    }

    @Override
    public boolean isEmpty() throws TemplateModelException {
        return map.isEmpty();
    }

    public void put(String key, TemplateModel model) {
        map.put(key, model);
    }

    public void putAll(Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            put(entry.getKey().toString(), FreemarkerUtils.convertItem(entry.getValue()));
        }
    }

}
