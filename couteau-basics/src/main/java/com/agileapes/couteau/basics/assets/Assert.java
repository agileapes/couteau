package com.agileapes.couteau.basics.assets;

import java.util.Collection;

/**
 * A simple assertion utility that helps with verification of input values
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/8/25, 16:53)
 */
public class Assert {

    /**
     * @param value    the value that must be false
     */
    public static void assertFalse(boolean value) {
        assertFalse(value, "Expected value to be false, while it was true");
    }

    /**
     * @param value      the value that must be false
     * @param message    the message
     */
    public static void assertFalse(boolean value, String message) {
        if (value) {
            throw new AssertionError(message);
        }
    }

    /**
     * @param value    the value that must be true
     */
    public static void assertTrue(boolean value) {
        assertTrue(value, "Expected value to be true, while it was false");
    }

    /**
     * @param value      the value that must be true
     * @param message    the message
     */
    public static void assertTrue(boolean value, String message) {
        if (!value) {
            throw new AssertionError(message);
        }
    }

    /**
     * @param value    checks that the value is not null
     */
    public static void assertNotNull(Object value) {
        assertNotNull(value, "Value cannot be null");
    }

    /**
     * @param value      checks that the value is not null
     * @param message    the message
     */
    public static void assertNotNull(Object value, String message) {
        assertFalse(value == null, message);
    }

    /**
     * @param value    checks that the value is null
     */
    public static void assertNull(Object value) {
        assertNull(value, "Value must be null");
    }

    /**
     * @param value      checks that the value is null
     * @param message    the message
     */
    public static void assertNull(Object value, String message) {
        assertTrue(value == null, message);
    }

    /**
     * @param value    the value to be checked
     * @param type     the expected type of the value
     */
    public static void assertInstanceOf(Object value, Class<?> type) {
        assertInstanceOf(value, type, "Expected value to be of type " + type.getCanonicalName() + " while it was " + value.getClass().getCanonicalName());
    }

    /**
     * @param value      the value to be checked
     * @param type       the expected type of the value
     * @param message    the message
     */
    public static void assertInstanceOf(Object value, Class<?> type, String message) {
        assertTrue(value == null || type.isInstance(value), message);
    }

    /**
     * Checks whether the given collection's size is between the boundaries. The boundaries are inclusive.
 * @param collection    the collection
     * @param minSize       minimum allowed size
 * @param maxSize       maximum allowed size
     */
    public static void assertCollectionSize(Collection<?> collection, int minSize, int maxSize) {
        assertCollectionSize(collection, minSize, maxSize, "Collection size must be in [" + minSize + "," + maxSize + "] but it was " + collection.size());
    }

    /**
     * Checks whether the given collection's size is between the boundaries. The boundaries are inclusive.
     * @param collection    the collection
     * @param minSize       minimum allowed size
     * @param maxSize       maximum allowed size
     * @param message       the message
     */
    public static void assertCollectionSize(Collection<?> collection, int minSize, int maxSize, String message) {
        assertTrue(collection.size() >= minSize && collection.size() <= maxSize, message);
    }

}
