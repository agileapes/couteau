package com.agileapes.couteau.freemarker.model;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

import java.util.ArrayList;
import java.util.List;

/**
 * This model extends the capabilities {@link TemplateMethodModelEx} so that
 * arguments passed to the execute method are of their actual types and not
 * the template representations of themselves.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/31, 16:46)
 */
public abstract class NativeMethodModel implements TemplateMethodModelEx {

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        final List<Object> list = new ArrayList<Object>(arguments.size());
        for (Object argument : arguments) {
            list.add(argument instanceof TemplateModel ? DeepUnwrap.unwrap((TemplateModel) argument) : argument);
        }
        return execute(list);
    }

    protected abstract Object execute(List<?> arguments);

}
