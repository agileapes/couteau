package com.agileapes.couteau.reflection.impl;

import com.agileapes.couteau.reflection.contract.BeanInitializer;
import com.agileapes.couteau.reflection.error.BeanInstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This implementation of {@link BeanInitializer} will allow for easy bean construction through
 * declared constructors.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/4/13, 6:55 PM)
 */
public class ConstructorBeanInitializer implements BeanInitializer {

    private final static Map<Class<?>, Class<?>> primitives = new HashMap<Class<?>, Class<?>>();
    static {
        primitives.put(int.class, Integer.class);
        primitives.put(float.class, Float.class);
        primitives.put(double.class, Double.class);
        primitives.put(long.class, Long.class);
        primitives.put(short.class, Short.class);
        primitives.put(char.class, Character.class);
        primitives.put(boolean.class, Boolean.class);
    }

    @Override
    public <E> E initialize(Class<E> type, Class[] argumentTypes, Object[] arguments) throws BeanInstantiationException {
        if (argumentTypes == null) {
            throw new BeanInstantiationException("Argument types cannot be null");
        }
        if (arguments == null) {
            throw new BeanInstantiationException("Arguments cannot be null");
        }
        if (argumentTypes.length != arguments.length) {
            throw new BeanInstantiationException("Argument types must match arguments");
        }
        for (int i = 0; i < argumentTypes.length; i++) {
            Class argumentType = primitives.containsKey(argumentTypes[i]) ? primitives.get(argumentTypes[i]) :  argumentTypes[i];
            final Object argument = arguments[i];
            if (!argumentType.isInstance(argument) && ((argumentType.isPrimitive() || argument != null))) {
                throw new BeanInstantiationException("Arguments do not match the provided types");
            }
        }
        if (Modifier.isAbstract(type.getModifiers())) {
            throw new BeanInstantiationException("Cannot instantiate abstract class: " + type.getCanonicalName());
        }
        if (type.isInterface()) {
            throw new BeanInstantiationException("Cannot instantiate interface: " + type.getCanonicalName());
        }
        if (!Modifier.isPublic(type.getModifiers())) {
            throw new BeanInstantiationException("Cannot instantiate a class unless it is public: " + type.getCanonicalName());
        }
        final Constructor<?>[] constructors = type.getDeclaredConstructors();
        final Set<Constructor<?>> candidates = new HashSet<Constructor<?>>();
        for (Constructor<?> constructor : constructors) {
            if (Modifier.isPublic(constructor.getModifiers()) && constructor.getParameterTypes().length == arguments.length) {
                boolean matches = true;
                for (int i = 0; i < arguments.length; i ++) {
                    if (!constructor.getParameterTypes()[i].isAssignableFrom(argumentTypes[i])) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    candidates.add(constructor);
                }
            }
        }
        if (candidates.isEmpty()) {
            throw new BeanInstantiationException("No matching constructor found for specified arguments: " + type.getCanonicalName());
        }
        if (candidates.size() > 1) {
            throw new BeanInstantiationException("Ambiguous call to constructors for " + type.getCanonicalName());
        }
        final Constructor<?> constructor = candidates.iterator().next();
        final Object instance;
        try {
            instance = constructor.newInstance(arguments);
        } catch (IllegalArgumentException e) {
            throw new BeanInstantiationException("Invalid arguments passed to constructor", e);
        } catch (InstantiationException e) {
            throw new BeanInstantiationException("Failed to instantiate bean of type " + type.getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            throw new BeanInstantiationException("Access denied to constructor: " + type.getCanonicalName(), e);
        } catch (InvocationTargetException e) {
            throw new BeanInstantiationException("Construction failed due to errors inside the constructor for type " + type.getCanonicalName(), e);
        }
        return type.cast(instance);
    }

}
