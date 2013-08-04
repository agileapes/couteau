/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.couteau.freemarker.conversion;

import com.agileapes.couteau.reflection.convert.ConversionDecision;
import com.agileapes.couteau.reflection.convert.ConversionStrategy;
import com.agileapes.couteau.reflection.property.PropertyDescriptor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import freemarker.template.TemplateModel;

import java.util.Date;

/**
 * This strategy is designed specifically for handling freemarker bean conversion
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/15/13, 6:21 PM)
 */
public class FreemarkerConversionStrategy implements ConversionStrategy {

    @Override
    public ConversionDecision decide(PropertyDescriptor propertyDescriptor) {
        final Class<?> propertyType = ReflectionUtils.mapType(propertyDescriptor.getType());
        if (TemplateModel.class.isAssignableFrom(propertyType)) {
            return ConversionDecision.PASS;
        }
        if (Class.class.isAssignableFrom(propertyType)) {
            return ConversionDecision.PASS;
        }
        if (Number.class.isAssignableFrom(propertyType)) {
            return ConversionDecision.PASS;
        }
        if (Boolean.class.isAssignableFrom(propertyType)) {
            return ConversionDecision.PASS;
        }
        if (String.class.isAssignableFrom(propertyType)) {
            return ConversionDecision.PASS;
        }
        if (Date.class.isAssignableFrom(propertyType)) {
            return ConversionDecision.PASS;
        }
        return ConversionDecision.CONVERT;
    }

}
