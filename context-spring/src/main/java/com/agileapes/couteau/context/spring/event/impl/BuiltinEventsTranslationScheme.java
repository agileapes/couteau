package com.agileapes.couteau.context.spring.event.impl;

import com.agileapes.couteau.context.contract.Event;
import com.agileapes.couteau.context.spring.event.TranslationScheme;
import com.agileapes.couteau.context.util.ClassUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/29/13, 4:51 PM)
 */
public class BuiltinEventsTranslationScheme implements TranslationScheme, ApplicationContextAware {

    public static final String BUILTIN_EVENTS_PACKAGE = "com.agileapes.couteau.context.event.";
    public static final String TRANSLATED_EVENTS_PACKAGE = "com.agileapes.couteau.context.spring.event.events.";
    private ClassLoader classLoader;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        classLoader = applicationContext.getClassLoader();
    }

    @Override
    public boolean handles(Event event) {
        return event.getClass().getCanonicalName().startsWith(BUILTIN_EVENTS_PACKAGE);
    }

    @Override
    public ApplicationEvent translate(Event originalEvent) {
        final Class<? extends ApplicationEvent> eventClass;
        final ApplicationEvent applicationEvent;
        try {
            //noinspection unchecked
            eventClass = ApplicationEvent.class.asSubclass(ClassUtils.forName(TRANSLATED_EVENTS_PACKAGE.concat(originalEvent.getClass().getSimpleName()), classLoader));
        } catch (ClassNotFoundException e) {
            return null;
        }
        try {
            applicationEvent = eventClass.getConstructor(Object.class).newInstance(originalEvent.getSource());
        } catch (Exception e) {
            return null;
        }
        for (Method method : originalEvent.getClass().getMethods()) {
            if (!GenericTranslationScheme.isGetter(method)) {
                continue;
            }
            final String fieldName = StringUtils.uncapitalize(method.getName().substring(method.getName().startsWith("get") ? 3 : 2));
            final Field field = ReflectionUtils.findField(eventClass, fieldName);
            if (field == null || !field.getType().isAssignableFrom(method.getReturnType())) {
                continue;
            }
            field.setAccessible(true);
            try {
                field.set(originalEvent, method.invoke(originalEvent));
            } catch (Exception ignored) {
            }
        }
        return applicationEvent;
    }

    @Override
    public void fillIn(Event originalEvent, ApplicationEvent translated) {
        for (Method method : originalEvent.getClass().getMethods()) {
            if (!method.getName().matches("set[A-Z].*") || !Modifier.isPublic(method.getModifiers())
                    || !method.getReturnType().equals(void.class) || method.getParameterTypes().length != 1) {
                continue;
            }
            final String propertyName = StringUtils.uncapitalize(method.getName().substring(3));
            final Field field = ReflectionUtils.findField(translated.getClass(), propertyName);
            if (field == null || !method.getParameterTypes()[0].isAssignableFrom(field.getType())) {
                continue;
            }
            try {
                method.invoke(originalEvent, field.get(translated));
            } catch (Exception ignored) {
            }
        }
    }

}
