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

import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.basics.api.Transformer;
import com.agileapes.couteau.concurrency.deferred.Failure;
import com.agileapes.couteau.concurrency.deferred.NotifiableDeferred;
import com.agileapes.couteau.concurrency.deferred.NotifiablePromise;
import com.agileapes.couteau.concurrency.deferred.impl.DeferredTask;
import com.agileapes.couteau.http.io.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/15 AD, 19:18)
 */
public class DownloadingTransformer implements Transformer<HttpResponse, HttpResponse> {

    private final InputStreamDownloader downloader;
    private final NotifiableDeferred<OutputStream, DownloadNotification> deferred;

    public DownloadingTransformer(OutputStream outputStream) {
        downloader = new InputStreamDownloader(outputStream);
        deferred = DeferredTask.defer(OutputStream.class, DownloadNotification.class);
        downloader.getPromise().then(new Processor<OutputStream>() {
            @Override
            public void process(OutputStream input) {
                deferred.resolve(input);
            }
        }, new Processor<Failure>() {
            @Override
            public void process(Failure input) {
                deferred.reject(input);
            }
        }, new Processor<DownloadNotification>() {
            @Override
            public void process(DownloadNotification input) {
                deferred.notify(input);
            }
        });
    }

    @Override
    public HttpResponse map(HttpResponse input) {
        try {
            downloader.process(input.getContent().getContent());
        } catch (IOException e) {
            deferred.reject("Failed to get HTTP input stream", e);
        }
        return input;
    }

    public NotifiablePromise<OutputStream, DownloadNotification> getPromise() {
        return deferred.getPromise();
    }

}
