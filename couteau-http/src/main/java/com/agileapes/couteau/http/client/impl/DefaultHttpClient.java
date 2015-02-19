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

package com.agileapes.couteau.http.client.impl;

import com.agileapes.couteau.concurrency.deferred.Deferred;
import com.agileapes.couteau.concurrency.deferred.Promise;
import com.agileapes.couteau.concurrency.deferred.impl.DeferredTask;
import com.agileapes.couteau.http.client.HttpClient;
import com.agileapes.couteau.http.client.LatentHttpClient;
import com.agileapes.couteau.http.io.HttpHeader;
import com.agileapes.couteau.http.io.HttpRequest;
import com.agileapes.couteau.http.io.HttpRequestMethod;
import com.agileapes.couteau.http.io.HttpResponse;
import com.agileapes.couteau.http.io.impl.ImmutableHttpRequest;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/15 AD, 17:16)
 */
public class DefaultHttpClient implements HttpClient, Closeable {

    private final LatentHttpClient httpClient;

    public DefaultHttpClient() {
        this(new DefaultLatentHttpClient());
    }

    public DefaultHttpClient(RequestConfig requestConfig) {
        this(new DefaultLatentHttpClient(requestConfig));
    }

    public DefaultHttpClient(LatentHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Promise<HttpResponse> send(HttpRequest request) {
        final Deferred<HttpResponse> deferred = DeferredTask.defer(HttpResponse.class);
        try {
            final HttpResponse response = httpClient.send(request).execute();
            if (response.getStatus() / 100 == 4 || response.getStatus() / 100 == 5) {
                deferred.reject(new HttpRequestFailure("Failed to execute HTTP request", null, request, response));
            } else {
                deferred.resolve(response);
            }
        } catch (Throwable e) {
            deferred.reject(new HttpRequestFailure("Failed to execute HTTP request", e, request, null));
        }
        return deferred.getPromise();
    }

    @Override
    public Promise<HttpResponse> head(URI uri) {
        return head(uri, null);
    }

    @Override
    public Promise<HttpResponse> head(URI uri, List<HttpHeader> headers) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.HEAD, null, headers, null));
    }

    @Override
    public Promise<HttpResponse> get(URI uri) {
        return get(uri, null);
    }

    @Override
    public Promise<HttpResponse> get(URI uri, List<HttpHeader> headers) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.GET, null, headers, null));
    }

    @Override
    public Promise<HttpResponse> delete(URI uri) {
        return delete(uri, null);
    }

    @Override
    public Promise<HttpResponse> delete(URI uri, List<HttpHeader> headers) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.DELETE, null, headers, null));
    }

    @Override
    public Promise<HttpResponse> put(URI uri, HttpEntity data) {
        return put(uri, null, data);
    }

    @Override
    public Promise<HttpResponse> put(URI uri, List<HttpHeader> headers, HttpEntity data) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.PUT, data, headers, null));
    }

    @Override
    public Promise<HttpResponse> post(URI uri, HttpEntity data) {
        return post(uri, null, data);
    }

    @Override
    public Promise<HttpResponse> post(URI uri, List<HttpHeader> headers, HttpEntity data) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.POST, data, headers, null));
    }

    @Override
    public Promise<HttpResponse> patch(URI uri, HttpEntity data) {
        return patch(uri, null, data);
    }

    @Override
    public Promise<HttpResponse> patch(URI uri, List<HttpHeader> headers, HttpEntity data) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.PATCH, data, headers, null));
    }

    @Override
    public Promise<HttpResponse> trace(URI uri) {
        return trace(uri, null);
    }

    @Override
    public Promise<HttpResponse> trace(URI uri, List<HttpHeader> headers) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.TRACE, null, headers, null));
    }

    @Override
    public Promise<HttpResponse> options(URI uri) {
        return options(uri, null);
    }

    @Override
    public Promise<HttpResponse> options(URI uri, List<HttpHeader> headers) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.OPTIONS, null, headers, null));
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

}
