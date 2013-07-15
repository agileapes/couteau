package com.agileapes.couteau.reflection.util;

import com.agileapes.couteau.basics.collections.CollectionWrapper;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * This is basically a collection of utility methods (functions) designed to help
 * with various tasks regarding the reflection framework in Java's running virtual
 * machine.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 2:15 PM)
 */
public abstract class ReflectionUtils {

    private final static Map<Class<?>, Class<?>> MAPPED_TYPES = new HashMap<Class<?>, Class<?>>();
    static {
        MAPPED_TYPES.put(int.class, Integer.class);
        MAPPED_TYPES.put(float.class, Float.class);
        MAPPED_TYPES.put(double.class, Double.class);
        MAPPED_TYPES.put(long.class, Long.class);
        MAPPED_TYPES.put(short.class, Short.class);
        MAPPED_TYPES.put(char.class, Character.class);
        MAPPED_TYPES.put(boolean.class, Boolean.class);
    }

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

    public static String getGetterName(String propertyName) {
        return "get" + propertyName.substring(0, 1).toUpperCase().concat(propertyName.substring(1));
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

    public static String getPropertyName(String methodName) {
        if (methodName.matches("(?:get|set)[A-Z].*")) {
            methodName = methodName.substring(3);
        } else if (methodName.matches("(?:is)[A-Z].*")) {
            methodName = methodName.substring(2);
        } else {
            throw new IllegalArgumentException();
        }
        return methodName.substring(0, 1).toLowerCase().concat(methodName.substring(1));
    }

    public static Class<?> mapType(Class<?> type) {
        if (MAPPED_TYPES.containsKey(type)) {
            return MAPPED_TYPES.get(type);
        } else {
            return type;
        }
    }

    public static Class[] resolveTypeArguments(Type type, int length) {
        final Class[] classes = new Class[length];
        int i = 0;
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            for (Type argument : parameterizedType.getActualTypeArguments()) {
                final Class resolved;
                if (argument instanceof Class) {
                    resolved = (Class) argument;
                } else {
                    resolved = Object.class;
                }
                classes[i ++] = resolved;
            }
        }
        for (int j = i; j < length; j ++) {
            classes[j] = Object.class;
        }
        return classes;
    }

}
