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

package com.agileaps.couteau.freemarker.conversion;

import com.agileapes.couteau.reflection.beans.BeanWrapper;
import com.agileapes.couteau.reflection.beans.BeanWrapperFactory;
import com.agileapes.couteau.reflection.beans.impl.MethodBeanWrapper;
import com.agileapes.couteau.reflection.convert.BeanConverter;
import com.agileapes.couteau.reflection.convert.GenericBeanConverter;
import com.agileapes.couteau.reflection.convert.impl.DefaultBeanConverter;
import com.agileapes.couteau.reflection.error.BeanConversionException;
import com.agileaps.couteau.freemarker.model.GenericFreemarkerModel;
import com.agileaps.couteau.freemarker.utils.FreemarkerUtils;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.MapModel;
import freemarker.template.SimpleCollection;
import freemarker.template.TemplateModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This generic bean converter will take an arbitrary object as input and return an instance of
 * {@link TemplateModel} reflecting the properties within that object. This will allow for easy
 * conversion of any Java bean into a valid input for a Freemarker template.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/15/13, 5:09 PM)
 */
public class FreemarkerModelConverter implements GenericBeanConverter<Object, TemplateModel> {

    private final BeanConverter converter;

    public FreemarkerModelConverter() {
        converter = new DefaultBeanConverter(new BeanWrapperFactory() {
            @Override
            public <E> BeanWrapper<E> getBeanWrapper(E bean) {
                if (bean instanceof GenericFreemarkerModel) {
                    //noinspection unchecked
                    return (BeanWrapper<E>) bean;
                }
                return new MethodBeanWrapper<E>(bean);
            }
        }, new FreemarkerConversionStrategy());
    }

    @Override
    public TemplateModel convert(Object bean) throws BeanConversionException {
        if (bean instanceof Collection<?>) {
            final Collection<?> collection = (Collection<?>) bean;
            final ArrayList<TemplateModel> models = new ArrayList<TemplateModel>();
            for (Object object : collection) {
                models.add(convert(object));
            }
            return new SimpleCollection(models);
        } else if (bean instanceof Map<?, ?>) {
            final HashMap<TemplateModel, TemplateModel> map = new HashMap<TemplateModel, TemplateModel>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) bean).entrySet()) {
                map.put(convert(entry.getKey()), convert(entry.getValue()));
            }
            return new MapModel(map, BeansWrapper.getDefaultInstance());
        } else if (FreemarkerUtils.canConvert(bean)) {
            return FreemarkerUtils.convertItem(bean);
        }
        return converter.convert(bean, GenericFreemarkerModel.class);
    }

}
