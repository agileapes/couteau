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

package com.mmnaseri.couteau.maven.sample.task;

import com.mmnaseri.couteau.basics.api.Filter;
import com.mmnaseri.couteau.basics.api.Processor;
import com.mmnaseri.couteau.basics.api.Transformer;
import com.mmnaseri.couteau.basics.api.impl.NullFilter;
import com.mmnaseri.couteau.freemarker.utils.FreemarkerUtils;
import com.mmnaseri.couteau.lang.model.ImmutablePojoModel;
import com.mmnaseri.couteau.maven.resource.ProjectResource;
import com.mmnaseri.couteau.maven.resource.ProjectResourceType;
import com.mmnaseri.couteau.maven.sample.SampleExecutor;
import com.mmnaseri.couteau.maven.sample.code.GeneratedCodeRepository;
import com.mmnaseri.couteau.maven.task.PluginTask;
import com.mmnaseri.couteau.reflection.beans.impl.MethodClassBeanDescriptor;
import com.mmnaseri.couteau.reflection.error.NoSuchPropertyException;
import org.apache.maven.plugin.MojoFailureException;

import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.Map;

import static com.mmnaseri.couteau.basics.collections.CollectionWrapper.with;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (8/4/13, 1:15 PM)
 */
public class CodeGenerationTask extends PluginTask<SampleExecutor> {

    private final GeneratedCodeRepository repository;

    public CodeGenerationTask(GeneratedCodeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(SampleExecutor executor) throws MojoFailureException {
        //noinspection unchecked
        with(executor.getProjectResources()
        ).keep(new Filter<ProjectResource>() {
            @Override
            public boolean accepts(ProjectResource item) {
                return ProjectResourceType.CLASS.equals(item.getType());
            }
        }).transform(new Transformer<ProjectResource, Class>() {
            @Override
            public Class map(ProjectResource input) {
                return input.getClassArtifact();
            }
        }).drop(new Filter<Class>() {
            @Override
            public boolean accepts(Class item) {
                return item.isInterface() || item.getCanonicalName() == null || Modifier.isAbstract(item.getModifiers());
            }
        }).drop(new Filter<Class>() {
            @Override
            public boolean accepts(Class item) {
                return item.getCanonicalName().startsWith("com.mmnaseri.couteau.maven.")
                    || item.getCanonicalName().startsWith("com.mmnaseri.couteau.reflection.")
                    || item.getCanonicalName().startsWith("com.mmnaseri.couteau.lang.")
                    || item.getCanonicalName().startsWith("com.mmnaseri.couteau.freemarker.")
                    || item.getCanonicalName().startsWith("com.mmnaseri.couteau.strings.");
            }
        }).transform(new Transformer<Class, MethodClassBeanDescriptor<?>>() {
            @Override
            public MethodClassBeanDescriptor<?> map(Class input) {
                //noinspection unchecked
                return new MethodClassBeanDescriptor(input);
            }
        }).transform(new Transformer<MethodClassBeanDescriptor<?>, ImmutablePojoModel>() {
            @Override
            public ImmutablePojoModel map(MethodClassBeanDescriptor<?> input) {
                final ImmutablePojoModel ImmutablePojoModel = new ImmutablePojoModel(input.getBeanType().getCanonicalName().concat("Accessor"));
                for (String propertyName : input.getPropertyNames()) {
                    try {
                        ImmutablePojoModel.addProperty(propertyName, input.getPropertyType(propertyName));
                    } catch (NoSuchPropertyException ignored) {
                    }
                }
                return ImmutablePojoModel;
            }
        }).transform(new Transformer<ImmutablePojoModel, Map.Entry<String, String>>() {
            @Override
            public Map.Entry<String, String> map(ImmutablePojoModel input) {
                System.out.println("Generated: " + input.getQualifiedName());
                try {
                    return new AbstractMap.SimpleEntry<String, String>(input.getQualifiedName(), FreemarkerUtils.writeString(input));
                } catch (Exception e) {
                    return null;
                }
            }
        })
        .drop(new NullFilter<Map.Entry<String, String>>())
        .each(new Processor<Map.Entry<String, String>>() {
            @Override
            public void process(Map.Entry<String, String> input) {
                repository.register(input.getKey(), input.getValue());
            }
        });
    }

}
