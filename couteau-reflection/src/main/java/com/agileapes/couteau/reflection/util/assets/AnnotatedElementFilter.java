package com.agileapes.couteau.reflection.util.assets;

import com.agileapes.couteau.basics.api.Filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Filters items by their annotations
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 2:21 PM)
 */
public class AnnotatedElementFilter implements Filter<AnnotatedElement> {

    private final Class<? extends Annotation>[] annotations;

    public AnnotatedElementFilter(Class<? extends Annotation>... annotations) {
        this.annotations = annotations;
    }

    @Override
    public boolean accepts(AnnotatedElement item) throws Exception {
        for (Class<? extends Annotation> annotation : annotations) {
            if (item.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }
}
