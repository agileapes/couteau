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

package com.agileapes.couteau.http.io.impl;

import com.agileapes.couteau.http.io.HttpHeader;
import com.agileapes.couteau.http.io.HttpRequest;
import com.agileapes.couteau.http.io.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/15 AD, 17:13)
 */
public class ImmutableHttpResponse implements HttpResponse {

    private final HttpEntity content;
    private final int status;
    private final String statusText;
    private final List<HttpHeader> headers;
    private final HttpRequest request;
    private final CloseableHttpResponse response;

    public ImmutableHttpResponse(HttpEntity content, int status, String statusText, List<HttpHeader> headers, HttpRequest request, CloseableHttpResponse response) {
        this.content = content;
        this.status = status;
        this.statusText = statusText;
        this.headers = headers;
        this.request = request;
        this.response = response;
    }

    @Override
    public HttpEntity getContent() {
        return content;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getStatusText() {
        return statusText;
    }

    @Override
    public List<HttpHeader> getHeaders() {
        return Collections.unmodifiableList(headers);
    }

    @Override
    public HttpRequest getRequest() {
        return new ImmutableHttpRequest(request);
    }

    @Override
    public void close() throws IOException {
        if (response.getEntity() != null) {
            EntityUtils.consume(response.getEntity());
        }
        response.close();
    }

}
