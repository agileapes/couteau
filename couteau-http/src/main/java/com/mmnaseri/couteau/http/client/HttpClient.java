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

package com.mmnaseri.couteau.http.client;

import com.mmnaseri.couteau.concurrency.deferred.Promise;
import com.mmnaseri.couteau.http.io.HttpHeader;
import com.mmnaseri.couteau.http.io.HttpRequest;
import com.mmnaseri.couteau.http.io.HttpResponse;
import org.apache.http.HttpEntity;

import java.net.URI;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/15 AD, 16:57)
 */
public interface HttpClient {

    Promise<HttpResponse> send(HttpRequest request);

    Promise<HttpResponse> head(URI uri);

    Promise<HttpResponse> head(URI uri, List<HttpHeader> headers);

    Promise<HttpResponse> get(URI uri);
    
    Promise<HttpResponse> get(URI uri, List<HttpHeader> headers);

    Promise<HttpResponse> delete(URI uri);

    Promise<HttpResponse> delete(URI uri, List<HttpHeader> headers);

    Promise<HttpResponse> put(URI uri, HttpEntity data);

    Promise<HttpResponse> put(URI uri, List<HttpHeader> headers, HttpEntity data);

    Promise<HttpResponse> post(URI uri, HttpEntity data);

    Promise<HttpResponse> post(URI uri, List<HttpHeader> headers, HttpEntity data);

    Promise<HttpResponse> patch(URI uri, HttpEntity data);

    Promise<HttpResponse> patch(URI uri, List<HttpHeader> headers, HttpEntity data);

    Promise<HttpResponse> trace(URI uri);

    Promise<HttpResponse> trace(URI uri, List<HttpHeader> headers);

    Promise<HttpResponse> options(URI uri);

    Promise<HttpResponse> options(URI uri, List<HttpHeader> headers);

}
