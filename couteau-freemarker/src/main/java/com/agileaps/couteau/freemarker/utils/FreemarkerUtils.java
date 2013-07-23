/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileaps.couteau.freemarker.utils;

import com.agileapes.couteau.basics.api.Transformer;
import com.agileapes.couteau.reflection.util.ClassUtils;
import com.agileaps.couteau.freemarker.conversion.FreemarkerModelConverter;
import com.agileaps.couteau.freemarker.model.SimpleClassModel;
import freemarker.cache.ClassTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BooleanModel;
import freemarker.ext.beans.DateModel;
import freemarker.ext.beans.MapModel;
import freemarker.template.*;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.agileapes.couteau.basics.collections.CollectionWrapper.with;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/15/13, 8:20 PM)
 */
public abstract class FreemarkerUtils {
    /**
     * <p>Initializes a Freemarker Configuration for any given class and sets it up so that it will
     * look into "<code>templatePath</code>" for templates.</p>
     * <p><strong>NB</strong>: To use a faster method for configuring Freemarker, use Spring Application Context
     * with <code>FreeMarkerConfigurationFactoryBean</code>.</<p>
     *
     * @param aClass       The class references for template loader
     * @param templatePath Relative path for FTL storage.<br/> <strong>Note</strong>: should be visible to project at runtime
     * @return Configuration object
     */
    public static Configuration getConfiguration(Class<?> aClass, String templatePath) {
        try {
            freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_NONE);
        } catch (ClassNotFoundException ignored) {
        }
        Configuration configuration = new Configuration();
        ClassTemplateLoader templateLoader = new ClassTemplateLoader(aClass, templatePath);
        configuration.setTemplateLoader(templateLoader);
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        return configuration;
    }

    /**
     * This will write the processed output for any given object/model into the specified writer instance
     * <strong>NB</strong> Do note that the object need not be an instance of {@link freemarker.template.TemplateModel} as with
     * the actual Freemarker template processor, however if it is, the conversion process will be foregone
     * and the result will be available sooner.
     * @param configuration    the Freemarker configuration to use
     * @param writer           the writer
     * @param templateName     the name of the template to look for
     * @param object           the object
     * @throws Exception
     */
    public static void write(Configuration configuration, Writer writer, String templateName, Object object) throws Exception {
        final Template template = configuration.getTemplate(templateName);
        TemplateModel model;
        if (object instanceof TemplateModel) {
            model = (TemplateModel) object;
        } else {
            model = new FreemarkerModelConverter().convert(object);
        }
        template.process(model, writer);
    }

    /**
     * This will write the processed output for any given object/model into the specified writer instance
     * <strong>NB</strong> see notes for {@link #write(freemarker.template.Configuration, java.io.Writer, String, Object)}<br/>
     * <strong>NB</strong> This method tries to configure the Freemarker engine as well as possible through
     * heuristics, however, it is slower for that, and might not be very accurate
     * @param writer          the writer
     * @param templateName    the template path
     * @param object          the object
     * @throws Exception
     */
    public static void write(Writer writer, String templateName, Object object) throws Exception {
        Class<?> target;
        try {
            throw new Exception();
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            int i = 0;
            while (stackTrace[i].getClassName().equals(FreemarkerUtils.class.getName())) {
                i ++;
            }
            target = ClassUtils.forName(stackTrace[i].getClassName(), ClassUtils.getDefaultClassLoader());
        }
        if (!templateName.startsWith(File.separator)) {
            templateName = File.separator.concat(templateName);
        }
        String templatePath = templateName.substring(0, templateName.lastIndexOf("/") + 1)
                .replace("\\/", "/").replace("\\", "/").replaceAll("//{2,}", "/");
        templateName = templateName.substring(templatePath.length());
        final Configuration configuration = getConfiguration(target, templatePath.trim());
        write(configuration, writer, templateName, object);
    }

    /**
     * This will act like {@link #write(freemarker.template.Configuration, java.io.Writer, String, Object)}, only, it will
     * use a {@link java.io.StringWriter} by default and return the string content
     * @param configuration    the Freemarker configuration to use
     * @param templateName     the name of the template
     * @param object           the object/model
     * @return the processed template's contents
     * @throws Exception
     */
    public static String writeString(Configuration configuration, String templateName, Object object) throws Exception {
        final StringWriter writer = new StringWriter();
        write(configuration, writer, templateName, object);
        return writer.toString();
    }

    /**
     * This will act much like {@link #write(java.io.Writer, String, Object)} and as such, the notes for that
     * method also apply to this case
     * @param templateName    the path to the template in question
     * @param object          the object/model
     * @return the processed template's contents
     * @throws Exception
     */
    public static String writeString(String templateName, Object object) throws Exception {
        final StringWriter writer = new StringWriter();
        write(writer, templateName, object);
        return writer.toString();
    }

    /**
     * This method will use the provided "object.xml.ftl" Freemarker template to generate a deep XML
     * representation for the given object
     * @param object    the object
     * @return the XML representation
     * @throws Exception
     */
    @Deprecated
    public static String getXml(Object object) throws Exception {
        return writeString(getConfiguration(FreemarkerUtils.class, "/"), "object.xml.ftl", object);
    }

    /**
     * This version will attempt to determine the template name
     * @param configuration    the configuration to use
     * @param writer           the writer to write to
     * @param object           the object model
     */
    public static void write(Configuration configuration, Writer writer, Object object) throws Exception {
        String templateName;
        if (object.getClass().isAnnotationPresent(com.agileaps.couteau.freemarker.api.Template.class)) {
            templateName = object.getClass().getAnnotation(com.agileaps.couteau.freemarker.api.Template.class).value();
        } else {
            templateName = object.getClass().getName();
        }
        write(configuration, writer, templateName, object);
    }

    /**
     * This version works much the same as others except it will try to guess the template name
     * @param writer    the writer to write to
     * @param object    the object to be injected
     * @throws Exception
     */
    public static void write(Writer writer, Object object) throws Exception {
        String templateName;
        if (object.getClass().isAnnotationPresent(com.agileaps.couteau.freemarker.api.Template.class)) {
            templateName = object.getClass().getAnnotation(com.agileaps.couteau.freemarker.api.Template.class).value();
        } else {
            templateName = object.getClass().getName();
        }
        write(writer, templateName, object);
    }

    /**
     * This method will take a configuration and an object as input and write the object into the configured
     * template, returning it as a String
     * @param configuration    the configuration
     * @param object           the input object
     * @return the string value resulting from the injection of the object into the template
     * @throws Exception
     */
    public static String writeString(Configuration configuration, Object object) throws Exception {
        final StringWriter writer = new StringWriter();
        write(configuration, writer, object);
        return writer.toString();
    }

    /**
     * This method will write the given object into the template that matches it best.
     * @param object    the object to be injected into a model
     * @return the string value of the processed template
     * @throws Exception
     */
    public static String writeString(Object object) throws Exception {
        final Writer writer = new StringWriter();
        write(writer, object);
        return writer.toString();
    }

    /**
     * Determines whether the input object can be readily converted using
     * {@link #convertItem(Object)}
     * @param input    the input object
     * @return {@code true} if it can be converted via the conversion utility method
     */
    public static boolean canConvert(Object input) {
        if (input instanceof TemplateModel) {
            return true;
        } else if (input instanceof Number) {
            return true;
        } else if (input instanceof Boolean) {
            return true;
        } else if (input instanceof String) {
            return true;
        } else if (input instanceof Date) {
            return true;
        } else if (input instanceof Class) {
            return true;
        } else if (input instanceof Collection) {
            for (Object item : ((Collection) input)) {
                if (!canConvert(item)) {
                    return false;
                }
            }
            return true;
        } else if (input instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) input).entrySet()) {
                if (!canConvert(entry.getKey()) || !canConvert(entry.getValue())) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>This method converts all input objects that are considered <em>essential primitives</em> of the
     * Java language.</p>
     * <p>This includes all numbers, String values, date objects, boolean values, and collections and maps of
     * these items.</p>
     * <p>If the input is already an instance of {@link TemplateModel} it will be returned immediately.</p>
     * @param input    the object to be converted.
     * @return the template model object representing the given value
     */
    public static TemplateModel convertItem(Object input) {
        TemplateModel model;
        if (input instanceof TemplateModel) {
            model = (TemplateModel) input;
        } else if (input instanceof Number) {
            model = new SimpleNumber((Number) input);
        } else if (input instanceof Boolean) {
            model = new BooleanModel((Boolean) input, BeansWrapper.getDefaultInstance());
        } else if (input instanceof String) {
            model = new SimpleScalar((String) input);
        } else if (input instanceof Date) {
            model = new DateModel((Date) input, BeansWrapper.getDefaultInstance());
        } else if (input instanceof Class) {
            model = new SimpleClassModel((Class) input);
        } else if (input instanceof Collection) {
            try {
                //noinspection unchecked
                model = new SimpleCollection(with(((Collection) input)).transform(new Transformer<Object, TemplateModel>() {
                    @Override
                    public TemplateModel map(Object input) throws Exception {
                        return convertItem(input);
                    }
                }).list());
            } catch (Exception e) {
                throw new IllegalArgumentException();
            }
        } else if (input instanceof Map) {
            final Map<TemplateModel, TemplateModel> map = new HashMap<TemplateModel, TemplateModel>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) input).entrySet()) {
                map.put(convertItem(entry.getKey()), convertItem(entry.getValue()));
            }
            model = new MapModel(map, BeansWrapper.getDefaultInstance());
        } else {
            throw new IllegalArgumentException();
        }
        return model;
    }

}
