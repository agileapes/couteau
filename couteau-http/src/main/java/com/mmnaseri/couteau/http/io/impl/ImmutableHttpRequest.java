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

package com.mmnaseri.couteau.http.io.impl;

import com.mmnaseri.couteau.basics.api.Transformer;
import com.mmnaseri.couteau.http.io.HttpHeader;
import com.mmnaseri.couteau.http.io.HttpRequest;
import com.mmnaseri.couteau.http.io.HttpRequestMethod;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import static com.mmnaseri.couteau.basics.collections.CollectionWrapper.with;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/15 AD, 17:07)
 */
public class ImmutableHttpRequest implements HttpRequest {

    private final HttpRequestMethod method;
    private final URI uri;
    private final Map<String, String> parameters;
    private final HttpEntity data;
    private final List<HttpHeader> headers;
    private final RequestConfig config;

    public ImmutableHttpRequest(HttpRequest request) {
        this.method = request.getMethod();
        this.uri = request.getURI();
        this.parameters = new HashMap<String, String>(request.getParameters() == null ? Collections.<String, String>emptyMap() : request.getParameters());
        this.data = request.getData();
        this.headers = new ArrayList<HttpHeader>(request.getHeaders() == null ? Collections.<HttpHeader>emptySet() : request.getHeaders());
        this.config = request.getConfig();
    }

    public ImmutableHttpRequest(URI uri, HttpRequestMethod method, HttpEntity data, List<HttpHeader> headers, RequestConfig requestConfig) {
        this.method = method;
        this.uri = uri;
        this.data = data;
        this.headers = headers == null ? Collections.<HttpHeader>emptyList() : with(headers).transform(new Transformer<HttpHeader, HttpHeader>() {
            @Override
            public HttpHeader map(HttpHeader input) {
                return new ImmutableHttpHeader(input.getName(), input.getValue());
            }
        }).list();
        this.config = requestConfig;
        parameters = new HashMap<String, String>();
        final List<Map.Entry<String, String>> entries = with((this.uri.getQuery() == null ? "" : this.uri.getQuery()).split("&")).transform(new Transformer<String, Map.Entry<String, String>>() {
            @Override
            public Map.Entry<String, String> map(String input) {
                final String[] strings = input.split("=", 2);
                return new AbstractMap.SimpleEntry<String, String>(strings[0], strings.length > 1 ? strings[1] : "");
            }
        }).list();
        for (Map.Entry<String, String> entry : entries) {
            parameters.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public HttpRequestMethod getMethod() {
        return method;
    }

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public URL getURL() {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    @Override
    public HttpEntity getData() {
        return data;
    }

    @Override
    public List<HttpHeader> getHeaders() {
        return Collections.unmodifiableList(headers);
    }

    @Override
    public RequestConfig getConfig() {
        return config;
    }

    @Override
    public String toString() {
        return getMethod().name() +  " " + getURI().getHost() + getURI().getPath() + (getURI().getQuery() != null && !getURI().getQuery().isEmpty() ? "?" + getURI().getQuery() : "");
    }
}
