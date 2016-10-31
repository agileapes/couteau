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

package com.mmnaseri.couteau.http.util;

import com.mmnaseri.couteau.basics.api.Processor;
import com.mmnaseri.couteau.concurrency.deferred.Deferred;
import com.mmnaseri.couteau.concurrency.deferred.Failure;
import com.mmnaseri.couteau.concurrency.deferred.NotifiablePromise;
import com.mmnaseri.couteau.concurrency.deferred.Promise;
import com.mmnaseri.couteau.concurrency.deferred.impl.DeferredTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/15 AD, 19:27)
 */
public class FileDownloadingTransformer extends DownloadingTransformer {

    private final Deferred<File> deferred;
    private final File file;

    public FileDownloadingTransformer(String filename) throws FileNotFoundException {
        this(new File(filename));
    }

    public FileDownloadingTransformer(File file) throws FileNotFoundException {
        super(new FileOutputStream(file));
        this.file = file;
        deferred = DeferredTask.defer(File.class);
    }

    @Override
    public NotifiablePromise<OutputStream, DownloadNotification> getPromise() {
        return super.getPromise().then(new Processor<OutputStream>() {
            @Override
            public void process(OutputStream input) {
                deferred.resolve(file);
            }
        }, new Processor<Failure>() {
            @Override
            public void process(Failure failure) {
                deferred.reject(failure);
            }
        });
    }

    public Promise<File> getFilePromise() {
        return deferred.getPromise();
    }

}
