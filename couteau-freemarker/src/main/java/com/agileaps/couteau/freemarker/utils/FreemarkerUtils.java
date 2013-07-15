package com.agileaps.couteau.freemarker.utils;

import com.agileapes.couteau.reflection.util.ClassUtils;
import com.agileaps.couteau.freemarker.conversion.FreemarkerModelConverter;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateModel;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;

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

    public static String writeString(Configuration configuration, Object object) throws Exception {
        final StringWriter writer = new StringWriter();
        write(configuration, writer, object);
        return writer.toString();
    }

    public static String writeString(Object object) throws Exception {
        final Writer writer = new StringWriter();
        write(writer, object);
        return writer.toString();
    }
}
