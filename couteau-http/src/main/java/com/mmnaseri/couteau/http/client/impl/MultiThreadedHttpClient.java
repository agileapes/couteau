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

import com.mmnaseri.couteau.concurrency.deferred.Promise;
import com.mmnaseri.couteau.concurrency.error.TaskContextException;
import com.mmnaseri.couteau.concurrency.error.TaskFailureException;
import com.mmnaseri.couteau.concurrency.manager.TaskManager;
import com.mmnaseri.couteau.concurrency.manager.TaskManagerStatus;
import com.mmnaseri.couteau.concurrency.manager.impl.ThreadPoolTaskManager;
import com.mmnaseri.couteau.concurrency.task.DeferredCallable;
import com.mmnaseri.couteau.concurrency.task.Task;
import com.mmnaseri.couteau.http.client.HttpClient;
import com.mmnaseri.couteau.http.client.LatentHttpClient;
import com.mmnaseri.couteau.http.error.HttpResponseError;
import com.mmnaseri.couteau.http.io.HttpRequest;
import com.mmnaseri.couteau.http.io.HttpResponse;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (14/6/15 AD, 19:48)
 */
public class MultiThreadedHttpClient extends DefaultHttpClient implements HttpClient, TaskManager {

    public static final String DEFAULT_NAME = "HttpClient";
    private final TaskManager taskManager;
    private final LatentHttpClient httpClient;
    private final String name;

    public MultiThreadedHttpClient() {
        this(new DefaultLatentHttpClient());
    }

    public MultiThreadedHttpClient(LatentHttpClient httpClient, String name) {
        this(new ThreadPoolTaskManager(name, Runtime.getRuntime().availableProcessors(), false), httpClient, name);
    }

    public MultiThreadedHttpClient(LatentHttpClient httpClient) {
        this(new ThreadPoolTaskManager(), httpClient, DEFAULT_NAME);
    }

    public MultiThreadedHttpClient(TaskManager taskManager) {
        this(taskManager, new DefaultLatentHttpClient(), taskManager.getName());
    }

    public MultiThreadedHttpClient(TaskManager taskManager, LatentHttpClient httpClient, String name) {
        this.taskManager = taskManager;
        this.httpClient = httpClient;
        this.name = name;
    }

    @Override
    public Promise<HttpResponse> send(final HttpRequest request) {
        return defer(new DeferredCallable<HttpResponse>() {
            @Override
            public HttpResponse execute() throws TaskFailureException {
                final DeferredCallable<HttpResponse> callable = httpClient.send(request);
                final HttpResponse response = callable.execute();
                if (response.getStatus() % 100 == 4 || response.getStatus() % 100 == 5) {
                    throw new TaskFailureException("HTTP request failed", new HttpResponseError(response.getStatus()));
                }
                return response;
            }
        });
    }

    @Override
    public void schedule(Task task) {
        taskManager.schedule(task);
    }

    @Override
    public <E> Promise<E> defer(DeferredCallable<E> task) {
        return taskManager.defer(task);
    }

    @Override
    public <E> Promise<E> defer(Promise<E> task) {
        return taskManager.defer(task);
    }

    @Override
    public void done(Task task) throws TaskContextException {
        taskManager.done(task);
    }

    @Override
    public void fail(Task task, Exception exception) throws TaskContextException {
        taskManager.fail(task, exception);
    }

    @Override
    public void shutdown() {
        taskManager.shutdown();
    }

    @Override
    public TaskManagerStatus getStatus() {
        return taskManager.getStatus();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void run() {
        taskManager.run();
    }

}
