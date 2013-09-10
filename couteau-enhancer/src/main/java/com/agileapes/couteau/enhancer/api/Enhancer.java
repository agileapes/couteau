package com.agileapes.couteau.enhancer.api;

/**
 * This interface will allow for creation of enhanced objects.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/10, 14:56)
 */
public interface Enhancer<E> {

    /**
     * Changes the superclass of the enhanced classes
     * @param superClass    the superclass
     */
    void setSuperClass(Class<? extends E> superClass);

    /**
     * Tells the enhancer which interfaces the enhanced class should implement
     * @param interfaces    the interfaces to implement
     */
    void setInterfaces(Class[] interfaces);

    /**
     * Changes the naming policy for the enhancement process
     * @param namingPolicy    the naming policy
     */

    void setNamingPolicy(NamingPolicy namingPolicy);

    /**
     * Changes the interceptor that will be injected into method
     * calls of the enhanced object
     * @param interceptor    the interceptor
     */
    void setInterceptor(MethodInterceptor interceptor);

    /**
     * @return an instance of the enhanced object created via the default constructor
     */
    E create();

    /**
     * @param argumentTypes           the types of the parameters
     * @param constructorArguments    the arguments to the constructor
     * @return an instance of the enhanced object via a constructor matching the given
     * argument types
     */
    E create(Class[] argumentTypes, Object[] constructorArguments);

}
