package com.agileapes.couteau.context.value;

import com.agileapes.couteau.context.error.ValueReaderError;

/**
 * <p>The value reader is designed to help with the process of converting String values into
 * objects represented by those values. Each implementation must announce whether or not it
 * can read values from String for any given type.</p>
 *
 * <p>Each value can have multiple representations. For instance, a key-value pair might be written
 * as either {@code <key, value>} or {@code key=value}. We can have two value readers, one for each
 * representation, and register them with the context. This way, the context will know that for
 * reading a value of key-value type it has two candidates, and will feed each candidate with the
 * representation at hand and the right one will eventually come up and translate the text into its
 * object value.</p>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/30, 13:50)
 */
public interface ValueReader {

    /**
     * Determines whether or not this value reader is capable of translating the textual representation
     * of a given type into its actual object value.
     * @param type    the type to be queried
     * @return {@code true} if this value reader can read the type in question
     */
    boolean canRead(Class<?> type);

    /**
     * Tries to read the value as given by the textual representation into an instance of the
     * given type
     * @param text    textual representation of the data
     * @param type    the type of data
     * @param <E>     the type parameter for the object instance
     * @return the object instance for the value represented by the input text
     * @throws ValueReaderError
     */
    <E> E read(String text, Class<E> type) throws ValueReaderError;

}
