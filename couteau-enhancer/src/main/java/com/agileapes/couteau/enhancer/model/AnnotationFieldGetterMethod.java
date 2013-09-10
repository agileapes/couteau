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

package com.agileapes.couteau.enhancer.model;

import com.agileapes.couteau.freemarker.api.Invokable;
import com.agileapes.couteau.freemarker.model.TypedMethodModel;

import java.lang.annotation.Annotation;

/**
 * This method model will return value from a field on a given annotation
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/10, 17:42)
 */
public class AnnotationFieldGetterMethod extends TypedMethodModel {

    @Invokable
    public String get(Annotation annotation, String field) {
        try {
            final Object value = annotation.annotationType().getMethod(field).invoke(annotation);
            if (value instanceof String) {
                return '"' + ((String) value).replaceAll("\\\\", "\\\\").replaceAll("\"", "\\\"") + '"';
            } else if (value instanceof Character) {
                final Character ch = (Character) value;
                if (ch == '\\') {
                    return "'\\\\'";
                } else if (ch == '\'') {
                    return "'\\''";
                }
            } else if (value instanceof Long) {
                return Long.toString((Long) value) + "L";
            } else if (value instanceof Double) {
                return Double.toString((Double) value) + "D";
            } else if (value instanceof Float) {
                return Float.toString((Float) value) + "F";
            }
            return value.toString();
        } catch (Exception e) {
            return null;
        }
    }

}
