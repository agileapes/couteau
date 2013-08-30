package com.agileapes.couteau.freemarker.model;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 * This is a model that allows statements inside a prefix such as <code>xyz</code> to be modeled into string values that
 * represent the parameter themselves. For instance, <code>${xyz.a.b}</code> will result into <code>${xyz.a.b}</code>. In
 * essence, this will allow for template processing for a certain namespace to be disabled.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
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
