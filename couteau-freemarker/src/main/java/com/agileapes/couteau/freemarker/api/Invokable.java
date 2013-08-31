package com.agileapes.couteau.freemarker.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation, when used on methods of a class extending the method model available
 * through {@link com.agileapes.couteau.freemarker.model.TypedMethodModel} will enable the
 * typed method to list all invokable methods for type-matching.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/31, 16:51)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Invokable {
}
