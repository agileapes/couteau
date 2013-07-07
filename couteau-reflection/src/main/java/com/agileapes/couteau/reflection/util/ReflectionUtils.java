package com.agileapes.couteau.reflection.util;

import com.agileapes.couteau.basics.collections.CollectionWrapper;
import com.agileapes.couteau.reflection.util.assets.MemberModifierFilter;
import com.agileapes.couteau.reflection.util.assets.MemberNameFilter;
import com.agileapes.couteau.reflection.util.assets.Modifiers;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is basically a collection of utility methods (functions) designed to help
 * with various tasks regarding the reflection framework in Java's running virtual
 * machine.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 2:15 PM)
 */
public abstract class ReflectionUtils {

    public static CollectionWrapper<Method> withMethods(Class<?> type) {
        final ArrayList<Method> methods = new ArrayList<Method>();
        while (type != null) {
            Collections.addAll(methods, type.getDeclaredMethods());
            type = type.getSuperclass();
        }
        return CollectionWrapper.with(methods);
    }

    public static CollectionWrapper<Field> withFields(Class<?> type) {
        final ArrayList<Field> fields = new ArrayList<Field>();
        while (type != null) {
            Collections.addAll(fields, type.getDeclaredFields());
            type = type.getSuperclass();
        }
        return CollectionWrapper.with(fields);
    }

    private static class Name {

        private String singular;
        private String plural;

        private Name(String singular) {
            this(singular, singular);
        }

        private Name(String singular, String plural) {
            this.singular = singular;
            this.plural = plural;
        }

        private String getSingular() {
            return singular;
        }

        private String getPlural() {
            return plural;
        }
    }

    /**
     * @param type    the type to be described
     * @return a textual, human-friendly description for the given type
     */
    public static String describeType(Class<?> type) {
        int array = 0;
        while (type.isArray()) {
            array ++;
            type = type.getComponentType();
        }
        final Name name;
        if (type.isEnum()) {
            final StringBuilder builder = new StringBuilder();
            Object[] enumConstants = type.getEnumConstants();
            builder.append("one of ");
            for (int i = 0; i < enumConstants.length; i++) {
                Object constant = enumConstants[i];
                builder.append("\"").append(constant.toString().toLowerCase()).append("\"");
                if (i < enumConstants.length - 2) {
                    builder.append(", ");
                } else if (i == enumConstants.length - 2) {
                    builder.append(", or ");
                }
            }
            name = new Name(builder.toString());
        } else if (java.util.Date.class.equals(type) || java.sql.Date.class.equals(type)) {
            name = new Name("a date formatted as: yyyy/mm/dd [hh:mm[:ss]]", "dates formatted as: yyyy/mm/dd [hh:mm[:ss]]");
        } else if (URL.class.equals(type)) {
            name = new Name("a valid URL", "valid URLs");
        } else if (URI.class.equals(type)) {
            name = new Name("a valid URI", "valid URIs");
        } else if (File.class.equals(type)) {
            name = new Name("a path to a file", "paths to files");
        } else if (Class.class.equals(type)) {
            name = new Name("fully qualified name of a class within the classpath (e.g. java.lang.String)",
                    "fully qualified names of classes within the classpath (e.g. java.lang.String)");
        } else {
            String expanded = "";
            final String simpleName = type.getSimpleName();
            for (int i = 0; i < simpleName.length(); i ++) {
                expanded += Character.toLowerCase(simpleName.charAt(i));
                if (Character.isLowerCase(simpleName.charAt(i)) && i < simpleName.length() - 1 && Character.isUpperCase(simpleName.charAt(i + 1))) {
                    expanded += " ";
                }
            }
            final String packageName = type.getName().contains(".") ? type.getName().substring(0, type.getName().lastIndexOf('.')) : "";
            if (!packageName.isEmpty() && !packageName.equals("java.lang")) {
                expanded = '"' + expanded + "\" under \"" + packageName + "\"";
                name = new Name("one " + expanded, "one or more " + expanded);
            } else {
                name = new Name(("aeiou".contains(String.valueOf(expanded.charAt(0))) ? "an " : "a ") + expanded,
                        "one or more " + expanded + "s");
            }
        }
        String value = array > 0 ? name.getPlural() : name.getSingular();
        while (array -- > 0) {
            if (array > 0) {
                value = "arrays of " + value;
            } else {
                value = "an array of " + value;
            }
        }
        return value;
    }

}
