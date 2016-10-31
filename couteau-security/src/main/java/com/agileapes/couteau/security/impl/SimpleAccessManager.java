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

package com.agileapes.couteau.security.impl;

import com.mmnaseri.couteau.basics.api.Filter;
import com.agileapes.couteau.context.impl.OrderedBeanComparator;
import com.agileapes.couteau.security.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.mmnaseri.couteau.basics.collections.CollectionWrapper.with;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/8/30 AD, 10:19)
 */
public class SimpleAccessManager implements AccessManager {

    private static final FailFastAccessDeniedHandler DEFAULT_ACCESS_DENIED_HANDLER = new FailFastAccessDeniedHandler();
    private static final AccessPolicy UNKNOWN_POLICY = new ImmutableAccessPolicy("(unknown policy)", Decision.DENY, new Filter<Actor>() {
        @Override
        public boolean accepts(Actor item) {
            return false;
        }
    }, new Filter<Subject>() {
        @Override
        public boolean accepts(Subject item) {
            return false;
        }
    });
    private final List<AccessDeniedHandler> handlers = new CopyOnWriteArrayList<AccessDeniedHandler>();
    private final List<AccessPolicy> policies = new CopyOnWriteArrayList<AccessPolicy>();

    @Override
    public synchronized void addPolicy(AccessPolicy policy) {
        policies.clear();
        policies.addAll(with(policies).add(policy).sort(new OrderedBeanComparator()).list());
    }

    @Override
    public void addAccessDeniedHandler(AccessDeniedHandler handler) {
        handlers.clear();
        handlers.addAll(with(handlers).add(handler).sort(new OrderedBeanComparator()).list());
    }

    @Override
    public void checkAccess(Actor actor, Subject subject) {
        final List<AccessPolicy> applyingPolicies = new ArrayList<AccessPolicy>();
        for (AccessPolicy policy : policies) {
            if (policy.getActorFilter().accepts(actor) && policy.getSubjectFilter().accepts(subject)) {
                applyingPolicies.add(policy);
            }
        }
        if (applyingPolicies.isEmpty()) {
            return;
        }
        Decision decision = Decision.UNDECIDED;
        AccessPolicy denyingPolicy = UNKNOWN_POLICY;
        for (AccessPolicy policy : applyingPolicies) {
            if (Decision.DENY.equals(policy.getDecision())) {
                denyingPolicy = policy;
                decision = Decision.DENY;
            } else if (Decision.GRANT.equals(policy.getDecision()) && Decision.UNDECIDED.equals(decision)) {
                decision = Decision.GRANT;
            }
        }
        if (decision.equals(Decision.GRANT)) {
            return;
        }
        for (AccessDeniedHandler handler : handlers) {
            if (handler.accepts(denyingPolicy)) {
                handler.handle(denyingPolicy, actor, subject);
                return;
            }
        }
        DEFAULT_ACCESS_DENIED_HANDLER.handle(denyingPolicy, actor, subject);
    }

}
