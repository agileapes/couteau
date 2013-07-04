package com.agileapes.couteau.context.value.impl;

import com.agileapes.couteau.context.error.InvalidInputValueError;
import com.agileapes.couteau.context.error.InvalidValueTypeError;
import com.agileapes.couteau.context.value.ValueReader;

import java.util.Arrays;
import java.util.List;

/**
 * This is a value reader capable of reading values for all the primitive value type defined
 * in Java.
 *
 * @see #classes See the types this reader understand
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/30, 13:57)
 */
public class PrimitiveValueReader implements ValueReader {

    @SuppressWarnings("unchecked")
    private final static List<? extends Class> classes = Arrays.asList(int.class, long.class, short.class, boolean.class, char.class,
            float.class, double.class, Integer.class, Long.class, Short.class, Boolean.class,
            Character.class, Float.class, Double.class, String.class);

    @Override
    public boolean canRead(Class<?> type) {
        return classes.contains(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> E read(String text, Class<E> type) {
        if (!canRead(type)) {
            throw new InvalidValueTypeError(type);
        }
        if (Number.class.isAssignableFrom(type)) {
            try {
                if (type.equals(int.class) || type.equals(Integer.class)) {
                    return (E) ((Integer) Integer.parseInt(text));
                } else if (type.equals(long.class) || type.equals(Long.class)) {
                    return (E) ((Long) Long.parseLong(text));
                } else if (type.equals(short.class) || type.equals(Short.class)) {
                    return (E) ((Short) Short.parseShort(text));
                } else if (type.equals(float.class) || type.equals(Float.class)) {
                    return (E) ((Float) Float.parseFloat(text));
                } else if (type.equals(double.class) || type.equals(Double.class)) {
                    return (E) ((Double) Double.parseDouble(text));
                } else {
                    throw new InvalidValueTypeError(type);
                }
            } catch (NumberFormatException e) {
                throw new InvalidInputValueError(text, type, e);
            }
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            if (Arrays.asList("1", "yes", "true", "on").contains(text.toLowerCase())) {
                return (E) ((Boolean) true);
            } else if (Arrays.asList("0", "no", "false", "off").contains(text.toLowerCase())) {
                return (E) ((Boolean) false);
            } else {
                throw new InvalidInputValueError(text, type);
            }
        } else if (type.equals(char.class) || type.equals(Character.class)) {
            if (text.length() != 1) {
                throw new IllegalArgumentException(text + " is not a valid character");
            }
            return (E) ((Character) text.charAt(0));
        } else {
            return (E) text;
        }
    }

}
