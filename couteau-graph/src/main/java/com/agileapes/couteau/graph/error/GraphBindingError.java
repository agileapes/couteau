package com.agileapes.couteau.graph.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/3/7 AD, 19:20)
 */
public class GraphBindingError extends Error {

    public GraphBindingError(String message) {
        super(message);
    }

    public GraphBindingError(String message, Throwable cause) {
        super(message, cause);
    }
}
