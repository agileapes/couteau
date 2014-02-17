package com.agileapes.couteau.graph.binding.impl.api;

import com.agileapes.couteau.graph.binding.BindingType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/2/17 AD, 18:08)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Query {

    String value();

    BindingType type() default BindingType.ELEMENT;

    String target() default "*";

}
