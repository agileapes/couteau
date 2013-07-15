package com.agileaps.couteau.freemarker.conversion;

import com.agileapes.couteau.reflection.beans.BeanWrapper;
import com.agileapes.couteau.reflection.beans.BeanWrapperFactory;
import com.agileapes.couteau.reflection.beans.impl.MethodBeanWrapper;
import com.agileapes.couteau.reflection.convert.BeanConverter;
import com.agileapes.couteau.reflection.convert.GenericBeanConverter;
import com.agileapes.couteau.reflection.convert.impl.DefaultBeanConverter;
import com.agileapes.couteau.reflection.error.BeanConversionException;
import com.agileaps.couteau.freemarker.model.GenericFreemarkerModel;
import freemarker.template.TemplateModel;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/15/13, 5:09 PM)
 */
public class FreemarkerModelConverter implements GenericBeanConverter<Object, TemplateModel> {

    private final BeanConverter converter = new DefaultBeanConverter(new BeanWrapperFactory() {
        @Override
        public <E> BeanWrapper<E> getBeanWrapper(E bean) {
            if (bean instanceof GenericFreemarkerModel) {
                //noinspection unchecked
                return (BeanWrapper<E>) bean;
            }
            return new MethodBeanWrapper<E>(bean);
        }
    }, new FreemarkerConversionStrategy());

    @Override
    public TemplateModel convert(Object bean) throws BeanConversionException {
        return converter.convert(bean, GenericFreemarkerModel.class);
    }

}
