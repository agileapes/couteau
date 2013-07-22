package com.agileapes.couteau.reflection.util.assets;

import com.agileapes.couteau.basics.api.Filter;

import java.lang.reflect.Member;

/**
 * Filters elements by their modifiers
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/6/13, 2:23 PM)
 */
public class MemberModifierFilter implements Filter<Member> {

    private final Modifiers[] modifiers;

    public MemberModifierFilter(Modifiers... modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public boolean accepts(Member item) throws Exception {
        for (Modifiers modifier : modifiers) {
            if (modifier.matches(item.getModifiers())) {
                return true;
            }
        }
        return false;
    }
}
