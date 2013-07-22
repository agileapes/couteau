package com.agileapes.couteau.reflection.util.assets;

import com.agileapes.couteau.basics.api.Filter;

import java.lang.reflect.Field;

/**
 * Accepts fields of a given type
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 5:29 PM)
 */
public class FieldTypeFilter implements Filter<Field> {

    private final Class<?> type;

    public FieldTypeFilter(Class<?> type) {
        this.type = type;
    }

    @Override
    public boolean accepts(Field item) throws Exception {
        return type.isAssignableFrom(item.getType());
    }

}
