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

package com.mmnaseri.couteau.http.util;

import com.mmnaseri.couteau.basics.api.Processor;
import com.mmnaseri.couteau.concurrency.deferred.NotifiableDeferred;
import com.mmnaseri.couteau.concurrency.deferred.NotifiablePromise;
import com.mmnaseri.couteau.concurrency.deferred.impl.DeferredTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (14/6/17 AD, 11:20)
 */
public class InputStreamDownloader implements Processor<InputStream> {

    private final OutputStream outputStream;
    private final NotifiableDeferred<OutputStream, DownloadNotification> deferred;

    public InputStreamDownloader(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.deferred = DeferredTask.defer(OutputStream.class, DownloadNotification.class);
    }

    @Override
    public void process(InputStream inputStream) {
        long totalRead = 0;
        try {
            int bufferSize = 128;
            byte[] buffer = new byte[bufferSize];
            int read;
            while ((read = inputStream.read(buffer, 0, bufferSize)) != -1) {
                totalRead += read;
                outputStream.write(buffer, 0, read);
                deferred.notify(new DownloadNotification(inputStream.available(), totalRead));
            }
            deferred.resolve(outputStream);
        } catch (Throwable e) {
            deferred.reject(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    deferred.reject("Failed to close output stream", e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    deferred.reject("Failed to close input stream", e);
                }
            }
        }
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public NotifiablePromise<OutputStream, DownloadNotification> getPromise() {
        return deferred.getPromise();
    }
}
