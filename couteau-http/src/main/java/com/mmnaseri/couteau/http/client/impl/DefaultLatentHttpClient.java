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

package com.mmnaseri.couteau.http.client.impl;

import com.mmnaseri.couteau.concurrency.error.TaskFailureException;
import com.mmnaseri.couteau.concurrency.task.DeferredCallable;
import com.mmnaseri.couteau.http.client.LatentHttpClient;
import com.mmnaseri.couteau.http.io.HttpHeader;
import com.mmnaseri.couteau.http.io.HttpRequest;
import com.mmnaseri.couteau.http.io.HttpRequestMethod;
import com.mmnaseri.couteau.http.io.HttpResponse;
import com.mmnaseri.couteau.http.io.impl.ImmutableHttpHeader;
import com.mmnaseri.couteau.http.io.impl.ImmutableHttpRequest;
import com.mmnaseri.couteau.http.io.impl.ImmutableHttpResponse;
import com.mmnaseri.couteau.reflection.beans.BeanInitializer;
import com.mmnaseri.couteau.reflection.beans.impl.ConstructorBeanInitializer;
import com.mmnaseri.couteau.reflection.error.BeanInstantiationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (14/6/15 AD, 18:37)
 */
public class DefaultLatentHttpClient implements LatentHttpClient {

    private final CloseableHttpClient httpClient;
    private final BeanInitializer initializer;
    private final static Log log = LogFactory.getLog(LatentHttpClient.class);
    private RequestConfig config;

    public DefaultLatentHttpClient() {
        this(RequestConfig.DEFAULT);
    }

    public DefaultLatentHttpClient(CloseableHttpClient httpClient) {
        this(httpClient, RequestConfig.DEFAULT);
    }

    public DefaultLatentHttpClient(CloseableHttpClient httpClient, RequestConfig config) {
        this.httpClient = httpClient;
        this.config = config;
        this.initializer = new ConstructorBeanInitializer();
    }

    public DefaultLatentHttpClient(RequestConfig config) {
        this(HttpClients.createDefault(), config);
    }

    @Override
    public void setConfig(RequestConfig config) {
        this.config = config;
    }

    private HttpRequestBase getRequest(HttpRequest httpRequest) throws BeanInstantiationException {
        final HttpRequestBase request = initializer.initialize(httpRequest.getMethod().getRequestType(), new Class[0]);
        request.setURI(httpRequest.getURI());
        if (httpRequest.getConfig() != null) {
            request.setConfig(httpRequest.getConfig());
        } else {
            request.setConfig(config);
        }
        for (HttpHeader header : httpRequest.getHeaders()) {
            request.setHeader(header.getName(), header.getValue());
        }
        if (request instanceof HttpEntityEnclosingRequest) {
            ((HttpEntityEnclosingRequest) request).setEntity(httpRequest.getData());
        }
        return request;
    }
    
    private HttpResponse getResponse(CloseableHttpResponse response, HttpRequest httpRequest) {
        final ArrayList<HttpHeader> headers = new ArrayList<HttpHeader>();
        for (Header header : response.getAllHeaders()) {
            headers.add(new ImmutableHttpHeader(header.getName(), header.getValue()));
        }
        return new ImmutableHttpResponse(response.getEntity(), response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(), headers, httpRequest, response);
    }

    @Override
    public DeferredCallable<HttpResponse> send(final HttpRequest httpRequest) {
        log.info("Preparing request <" + httpRequest + ">");
        return new DeferredCallable<HttpResponse>() {
            @Override
            public HttpResponse execute() throws TaskFailureException {
                final CloseableHttpResponse httpResponse;
                try {
                    log.info("Executing request <" + httpRequest + ">");
                    httpResponse = httpClient.execute(getRequest(httpRequest));
                    log.info("Request successfully retrieved");
                } catch (Throwable e) {
                    log.error(e);
                    throw new TaskFailureException("Failed to execute HTTP request", e);
                }
                return getResponse(httpResponse, httpRequest);
            }
        };
    }

    @Override
    public DeferredCallable<HttpResponse> get(URI uri) {
        return get(uri, null);
    }

    @Override
    public DeferredCallable<HttpResponse> get(URI uri, List<HttpHeader> headers) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.GET, null, headers, config));
    }

    @Override
    public DeferredCallable<HttpResponse> head(URI uri) {
        return head(uri, null);
    }

    @Override
    public DeferredCallable<HttpResponse> head(URI uri, List<HttpHeader> headers) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.HEAD, null, headers, config));
    }

    @Override
    public DeferredCallable<HttpResponse> delete(URI uri) {
        return delete(uri, null);
    }

    @Override
    public DeferredCallable<HttpResponse> delete(URI uri, List<HttpHeader> headers) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.DELETE, null, headers, config));
    }

    @Override
    public DeferredCallable<HttpResponse> trace(URI uri) {
        return trace(uri, null);
    }

    @Override
    public DeferredCallable<HttpResponse> trace(URI uri, List<HttpHeader> headers) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.TRACE, null, headers, config));
    }

    @Override
    public DeferredCallable<HttpResponse> options(URI uri) {
        return options(uri, null);
    }

    @Override
    public DeferredCallable<HttpResponse> options(URI uri, List<HttpHeader> headers) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.OPTIONS, null, headers, config));
    }

    @Override
    public DeferredCallable<HttpResponse> put(URI uri, HttpEntity data) {
        return put(uri, null, data);
    }

    @Override
    public DeferredCallable<HttpResponse> put(URI uri, List<HttpHeader> headers, HttpEntity data) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.PUT, null, headers, config));
    }

    @Override
    public DeferredCallable<HttpResponse> post(URI uri, HttpEntity data) {
        return post(uri, null, data);
    }

    @Override
    public DeferredCallable<HttpResponse> post(URI uri, List<HttpHeader> headers, HttpEntity data) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.POST, null, headers, config));
    }

    @Override
    public DeferredCallable<HttpResponse> patch(URI uri, HttpEntity data) {
        return patch(uri, null, data);
    }

    @Override
    public DeferredCallable<HttpResponse> patch(URI uri, List<HttpHeader> headers, HttpEntity data) {
        return send(new ImmutableHttpRequest(uri, HttpRequestMethod.PATCH, null, headers, config));
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

}
