package com.agileapes.couteau.freemarker.model;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Unresolved map model that allows dynamic expressions to be written more freely inside
 * a Freemarker template. This extension allows for statements such as <code>${x[y].z}</code>
 * to be automatically translated into <code>${x.y.z}</code> whenever this model is used
 * with the prefix <code>x</code>.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/30, 16:36)
 */
public class UnresolvedMapModel implements TemplateHashModel {

    private final String prefix;

    public UnresolvedMapModel(String prefix) {
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

}
