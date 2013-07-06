package com.agileapes.couteau.reflection.util.assets;

import com.agileapes.couteau.basics.api.Filter;

import java.lang.reflect.Member;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 2:20 PM)
 */
public class MemberNameFilter implements Filter<Member> {

    private final String[] patterns;

    public MemberNameFilter(String... patterns) {
        this.patterns = patterns;
    }

    @Override
    public boolean accepts(Member item) throws Exception {
        for (String pattern : patterns) {
            if (item.getName().matches(pattern)) {
                return true;
            }
        }
        return false;
    }

}
