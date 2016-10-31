/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agileapes.couteau.http.util;

import com.mmnaseri.couteau.basics.api.Filter;
import com.mmnaseri.couteau.basics.api.Transformer;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import com.agileapes.couteau.reflection.util.assets.MemberModifierFilter;
import com.agileapes.couteau.reflection.util.assets.Modifiers;
import org.apache.http.HttpStatus;

import java.lang.reflect.Field;

import static com.mmnaseri.couteau.basics.collections.CollectionWrapper.with;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/21 AD, 15:02)
 */
public class HttpStatusLine {

    public static final String UNKNOWN_ERROR = "Unknown Error";
    public static final String STATUS_LINE = "HTTP Error %d - %s";
    private final int status;
    private final String statusText;

    public HttpStatusLine(final int status) {
        this.status = status;
        //noinspection unchecked
        statusText = ReflectionUtils.withFields(HttpStatus.class)
                .keep(new MemberModifierFilter(Modifiers.STATIC))
                .keep(new Filter<Field>() {
                    @Override
                    public boolean accepts(Field item) {
                        try {
                            return item.get(null).equals(status);
                        } catch (IllegalAccessException e) {
                            return false;
                        }
                    }
                })
                .transform(new Transformer<Field, String>() {
                    @Override
                    public String map(Field input) {
                        final String[] words = input.getName().toLowerCase().substring(3) //"SC_".length()
                                .split("_+");
                        return with(words).transform(new Transformer<String, String>() {
                            @Override
                            public String map(String input) {
                                return input.length() > 1 ? input.substring(0, 1).toUpperCase() + input.substring(1) : input;
                            }
                        }).join(" ");
                    }
                }).first();
    }

    public int getStatusCode() {
        return status;
    }

    public String getStatusText() {
        return statusText == null ? UNKNOWN_ERROR : statusText;
    }

    public String getStatus() {
        return String.format(STATUS_LINE, getStatusCode(), getStatusText());
    }

}
