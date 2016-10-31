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

package com.mmnaseri.couteau.reflection.util;

import com.mmnaseri.couteau.basics.collections.CollectionWrapper;
import com.mmnaseri.couteau.reflection.util.assets.FieldTypeFilter;
import com.mmnaseri.couteau.reflection.util.assets.MemberNameFilter;
import com.mmnaseri.couteau.reflection.util.assets.MethodArgumentsFilter;

import java.io.File;
import java.lang.reflect.*;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

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

    /**
     * Wraps the declared methods of the given type in a {@link CollectionWrapper}. The methods
     * occur in successive order by the order of precedence, meaning that methods closer to this
     * type occur sooner. This is to preserve method overriding behavior.
     * @param type    the type being introspected
     * @return the collection wrapper for the type's methods
     */
    public static CollectionWrapper<Method> withMethods(Class<?> type) {
        final ArrayList<Method> methods = new ArrayList<Method>();
        while (type != null) {
            Collections.addAll(methods, type.getDeclaredMethods());
            type = type.getSuperclass();
        }
        return CollectionWrapper.with(methods);
    }

    /**
     * Wraps the fields of the given type and all its super classes. This method also preserves the
     * override precedence.
     * @param type    the type whose fields are being introspected
     * @return the collection wrapper for gathered fields
     * @see #withMethods(Class)
     */
    public static CollectionWrapper<Field> withFields(Class<?> type) {
        final ArrayList<Field> fields = new ArrayList<Field>();
        while (type != null) {
            Collections.addAll(fields, type.getDeclaredFields());
            type = type.getSuperclass();
        }
        return CollectionWrapper.with(fields);
    }

    /**
     * This class is used for conveniently describing an object by referring to it in a singular or plural form.
     * @see #describeType(Class)
     */
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

    /**
     * Returns the name of the property based on the accessor name for the property
     * @param methodName    the name of the accessor method
     * @return the property name
     */
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

    /**
     * Maps types to one another. This is primarily used to map primitive types to their object-oriented
     * equivalents.
     * @param type    the type being mapped.
     * @return the mapped type (or the actual type, if no mapping has been necessary).
     */
    public static <E> Class<? extends E> mapType(Class<? extends E> type) {
        if (MAPPED_TYPES.containsKey(type)) {
            //noinspection unchecked
            return (Class<? extends E>) MAPPED_TYPES.get(type);
        } else {
            return type;
        }
    }

    /**
     * Resolves type arguments for a generic type. The given length indicates how many generic
     * types are expected.
     * @param type      the type being evaluated.
     * @param length    the number of expected types
     * @return an array of generic types
     */
    public static Class[] resolveTypeArguments(Type type, int length) {
        final Class[] classes = new Class[length];
        int i = 0;
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            for (Type argument : parameterizedType.getActualTypeArguments()) {
                classes[i ++] = getClassOf(argument);
            }
        }
        for (int j = i; j < length; j ++) {
            classes[j] = Object.class;
        }
        return classes;
    }

    private static Class<?> getClassOf(Type type) {
        final Class<?> resolved;
        if (type instanceof Class) {
            resolved = (Class) type;
        } else if (type instanceof WildcardType && ((WildcardType) type).getUpperBounds().length > 0) {
            resolved = getClassOf(((WildcardType) type).getUpperBounds()[0]);
        } else {
            resolved = Object.class;
        }
        return resolved;
    }

    /**
     * Returns the array dimensions for the given class object, or {@code 0} if it is not an array
     * @param arrayType    the array type
     * @return the dimensions for the array
     */
    public static int getArrayDimensions(Class<?> arrayType) {
        if (arrayType.isArray()) {
            return 1 + getArrayDimensions(arrayType.getComponentType());
        } else {
            return 0;
        }
    }

    /**
     * Returns the component type of a multi-dimension array or the input class itself it it is not
     * an array type
     * @param arrayType    the array type
     * @return the component type of the array
     */
    public static Class<?> getComponentType(Class<?> arrayType) {
        return arrayType.isArray() ? getComponentType(arrayType.getComponentType()) : arrayType;
    }

    public static Class<?> getDeclaringClass(Member member) {
        final Class<?> declaringClass = member.getDeclaringClass();
        final List<Class<?>> superTypes = CollectionWrapper.with(declaringClass.getInterfaces()).add(declaringClass.getSuperclass()).list();
        for (Class<?> superType : superTypes) {
            final Member declaration = getDeclaration(member, superType);
            if (declaration != null) {
                return declaration.getDeclaringClass();
            }
        }
        return declaringClass;
    }

    public static Member getDeclaration(Member member, Class<?> superType) {
        if (member instanceof Method) {
            return CollectionWrapper.with(superType.getDeclaredMethods())
                    .keep(new MemberNameFilter(member.getName()))
                    .keep(new MethodArgumentsFilter(((Method) member).getParameterTypes()))
                    .first();
        } else if (member instanceof Field) {
            return CollectionWrapper.with(superType.getDeclaredFields())
                    .keep(new MemberNameFilter(member.getName()))
                    .keep(new FieldTypeFilter(((Field) member).getType()))
                    .first();
        }
        return null;
    }

    public static Collection<Object> getCollection(Class<?> propertyType) {
        if (!Collection.class.isAssignableFrom(propertyType)) {
            throw new Error("Expected property to be a collection while it was " + propertyType.getCanonicalName());
        }
        if (Set.class.isAssignableFrom(propertyType)) {
            if (TreeSet.class.isAssignableFrom(propertyType)) {
                return new TreeSet<Object>();
            } else if (ConcurrentSkipListSet.class.isAssignableFrom(propertyType)) {
                return new ConcurrentSkipListSet<Object>();
            } else if (CopyOnWriteArraySet.class.isAssignableFrom(propertyType)) {
                return new CopyOnWriteArraySet<Object>();
            }
            return new HashSet<Object>();
        } else if (List.class.isAssignableFrom(propertyType)) {
            if (LinkedList.class.isAssignableFrom(propertyType)) {
                return new LinkedList<Object>();
            } else if (CopyOnWriteArrayList.class.isAssignableFrom(propertyType)) {
                return new CopyOnWriteArrayList<Object>();
            }
            return new ArrayList<Object>();
        } else if (Queue.class.isAssignableFrom(propertyType)) {
            if (ConcurrentLinkedQueue.class.isAssignableFrom(propertyType)) {
                return new ConcurrentLinkedQueue<Object>();
            } else if (PriorityQueue.class.isAssignableFrom(propertyType)) {
                return new PriorityQueue<Object>();
            } else if (ConcurrentLinkedQueue.class.isAssignableFrom(propertyType)) {
                return new ConcurrentLinkedQueue<Object>();
            } else if (LinkedBlockingQueue.class.isAssignableFrom(propertyType)) {
                return new LinkedBlockingQueue<Object>();
            } else if (SynchronousQueue.class.isAssignableFrom(propertyType)) {
                return new SynchronousQueue<Object>();
            } else if (PriorityBlockingQueue.class.isAssignableFrom(propertyType)) {
                return new PriorityBlockingQueue<Object>();
            }
            return new PriorityQueue<Object>();
        } else if (Collection.class.equals(propertyType)) {
            return new ArrayList<Object>();
        } else {
            throw new UnsupportedOperationException("Cannot instantiate a collection of type " + propertyType.getCanonicalName());
        }
    }
}
