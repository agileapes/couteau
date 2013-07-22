package com.agileapes.couteau.reflection.convert;

/**
 * This enum abstracts a decision at conversion time.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/13/13, 7:32 AM)
 */
public enum  ConversionDecision {

    /**
     * This means that the item being contemplated should be passed to the target object
     * as-is, without any conversion
     */
    PASS,
    /**
     * This means that the item being contemplated should be dropped and not included in the
     * target object
     */
    IGNORE,
    /**
     * This means that a deep conversion should be queued for the given object
     */
    CONVERT

}
