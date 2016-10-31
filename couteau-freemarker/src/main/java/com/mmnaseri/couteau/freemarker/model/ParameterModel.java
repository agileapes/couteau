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

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 * This is a model that allows statements inside a prefix such as <code>xyz</code> to be modeled into string values that
 * represent the parameter themselves. For instance, <code>${xyz.a.b}</code> will result into <code>${xyz.a.b}</code>. In
 * essence, this will allow for template processing for a certain namespace to be disabled.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (2013/8/30, 15:57)
 */
public class ParameterModel implements TemplateHashModel, TemplateScalarModel {

    private final String prefix;

    public ParameterModel(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public TemplateModel get(String key) throws TemplateModelException {
        return new ParameterModel(prefix + "." + key);
    }

    @Override
    public boolean isEmpty() throws TemplateModelException {
        return false;
    }

    @Override
    public String getAsString() throws TemplateModelException {
        return "${" + prefix + "}";
    }

}
