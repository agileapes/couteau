package com.agileaps.couteau.freemarker.conversion;

import com.agileapes.couteau.reflection.convert.ConversionDecision;
import com.agileapes.couteau.reflection.convert.ConversionStrategy;
import com.agileapes.couteau.reflection.property.PropertyDescriptor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import freemarker.template.TemplateModel;

import java.util.Date;

/**
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
