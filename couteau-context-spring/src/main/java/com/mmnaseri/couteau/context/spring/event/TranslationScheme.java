/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
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

package com.mmnaseri.couteau.context.spring.event;

import com.mmnaseri.couteau.context.contract.Event;
import com.mmnaseri.couteau.context.spring.error.EventTranslationException;
import org.springframework.context.ApplicationEvent;

/**
 * <p>The translation scheme is n abstraction of the unit of work that is required for translating a Couteau
 * event object into a Spring event object. This is accomplished by first asking the scheme whether it can
 * handle a certain, given event, and then asking it to translate it into an instance of {@link ApplicationEvent}.</p>
 *
 * <p>The best practice here is to have the translated event extend {@link TranslatedEvent} so that
 * listeners might have a unified way of identifying which events are the actual events and which
 * events are translations.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (6/29/13, 4:16 PM)
 */
public interface TranslationScheme {

    /**
     * Determines whether or not the current scheme can handle the translation of a given event
     * @param event    the event object
     * @return {@code true} if the event can be translated using this scheme
     */
    boolean handles(Event event);

    /**
     * Translates the given event into an instance of {@link ApplicationEvent}
     * @param originalEvent    the original event
     * @return the translated event
     * @throws EventTranslationException
     */
    ApplicationEvent translate(Event originalEvent) throws EventTranslationException;

    /**
     * Translates back all the changes made to the translated event throughout its publication
     * into the original event to reflect all the changes,
     * @param originalEvent    the original event fired through Couteau
     * @param translated       the response from Spring
     * @throws EventTranslationException
     */
    void fillIn(Event originalEvent, ApplicationEvent translated) throws EventTranslationException;

}
