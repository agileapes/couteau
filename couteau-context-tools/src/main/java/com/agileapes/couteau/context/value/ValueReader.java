package com.agileapes.couteau.context.value;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/30, 13:50)
 */
public interface ValueReader {

    boolean canRead(Class<?> type);

    <E> E read(String text, Class<E> type);

}
