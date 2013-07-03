package com.agileapes.couteau.context.error;

/**
 * This indicates that an invalid name has been assigned to the bean. The details of the
 * exception must be sought out in the message.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 4:38 PM)
 */
public class InvalidBeanNameException extends RegistryException {

    public InvalidBeanNameException(String msg) {
        super(msg);
    }

}
