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

import com.mmnaseri.couteau.basics.api.Transformer;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.mmnaseri.couteau.basics.collections.CollectionWrapper.with;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (14/6/15 AD, 16:16)
 */
public class URIBuilder {

    public static enum Scheme {
        HTTP("http"), HTTPS("https");

        private final String name;

        Scheme(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    private String fragment = "";
    private String host = "localhost";
    private String path = "/";
    private String port = "";
    private Scheme scheme = Scheme.HTTP;
    private Map<String, String> query = new HashMap<String, String>();
    private String username = "";
    private String password = "";

    public URIBuilder setFragment(String fragment) {
        if (fragment == null) {
            fragment = "";
        }
        if (fragment.startsWith("#")) {
            fragment = fragment.substring(1);
        }
        this.fragment = fragment;
        return this;
    }

    public URIBuilder setHost(String host) throws URISyntaxException {
        if (host == null) {
            host = "";
        }
        if (host.isEmpty()) {
            throw new URISyntaxException(host, "Host cannot be empty");
        }
        this.host = host;
        return this;
    }

    public URIBuilder setPath(String path) {
        if (path == null) {
            path = "";
        }
        if (!path.startsWith("/")) {
            this.path = "/" + path;
        }
        return this;
    }

    public URIBuilder setPort(int port) {
        if (port < 0) {
            port = 0;
        }
        setPort(Integer.toString(port));
        return this;
    }

    public URIBuilder setPort(String port) {
        if (port == null) {
            port = "";
        }
        if (port.isEmpty()) {
            port = "80";
        }
        this.port = port;
        return this;
    }

    public URIBuilder setScheme(Scheme scheme) {
        if (scheme == null) {
            scheme = Scheme.HTTP;
        }
        this.scheme = scheme;
        return this;
    }

    public URIBuilder setUsername(String username) {
        if (username == null) {
            username = "";
        }
        this.username = username;
        return this;
    }

    public URIBuilder setPassword(String password) {
        if (password == null) {
            password = "";
        }
        this.password = password;
        return this;
    }

    public URIBuilder addQueryParameter(String name, String value) {
        if (value == null) {
            value = "";
        }
        if (name == null) {
            name = value;
        }
        if (name.isEmpty()) {
            return this;
        }
        query.put(name, value);
        return this;
    }

    public URIBuilder addQueryParameter(String name) {
        return addQueryParameter(name, null);
    }

    public URIBuilder addQueryParameter(String name, Object value) {
        return addQueryParameter(name, value == null ? null : value.toString());
    }

    public URI build() throws URISyntaxException {
        final StringBuilder builder = new StringBuilder();
        builder.append(scheme.getName()).append("://");
        if (!username.isEmpty()) {
            builder.append(username);
            if (!password.isEmpty()) {
                builder.append(":").append(password);
            }
            builder.append("@");
        }
        builder.append(host);
        if (port != null && !port.isEmpty()) {
            builder.append(":");
            builder.append(port);
        }
        builder.append(path);
        if (!query.isEmpty()) {
            builder.append("?").append(with(query.entrySet()).transform(new Transformer<Map.Entry<String, String>, String>() {
                @Override
                public String map(Map.Entry<String, String> input) {
                    return input.getKey() + (input.getValue().isEmpty() ? "" : "=") + input.getValue();
                }
            }).join("&"));
        }
        if (!fragment.isEmpty()) {
            builder.append("#").append(fragment);
        }
        try {
            final URL url = new URL(builder.toString());
            return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        } catch (MalformedURLException e) {
            throw new URISyntaxException(builder.toString(), "Invalid URI specification");
        }
    }

    public URIBuilder copy() {
        final URIBuilder builder = new URIBuilder();
        builder.fragment = fragment;
        builder.host = host;
        builder.path = path;
        builder.port = port;
        builder.scheme = scheme;
        builder.query = new HashMap<String, String>(query);
        builder.username = username;
        builder.password = password;
        return builder;
    }

}
