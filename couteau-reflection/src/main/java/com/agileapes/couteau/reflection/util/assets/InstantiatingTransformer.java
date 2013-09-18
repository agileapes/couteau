package com.agileapes.couteau.reflection.util.assets;

import com.agileapes.couteau.basics.api.Transformer;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/17, 18:45)
 */
public class InstantiatingTransformer<I> implements Transformer<Class<? extends I>, I> {

    @Override
    public I map(Class<? extends I> input) {
        try {
            return input.newInstance();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}
