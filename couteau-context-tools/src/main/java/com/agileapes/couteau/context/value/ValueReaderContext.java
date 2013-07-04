package com.agileapes.couteau.context.value;

import com.agileapes.couteau.context.contract.Context;

/**
 * The value reader context is the one value reader to rule them all. It will aggregate
 * all value readers into a single value reader that has the capability of all of them
 * in one place.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/30, 14:06)
 */
public interface ValueReaderContext extends ValueReader, Context<ValueReader> {
}
