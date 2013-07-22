package com.agileaps.couteau.freemarker.model;

import com.agileapes.couteau.reflection.beans.BeanWrapper;
import com.agileapes.couteau.reflection.beans.impl.MethodBeanWrapper;
import com.agileapes.couteau.reflection.convert.ConversionDecision;
import com.agileapes.couteau.reflection.convert.ConversionTarget;
import com.agileapes.couteau.reflection.error.NoSuchPropertyException;
import com.agileapes.couteau.reflection.error.PropertyAccessException;
import com.agileapes.couteau.reflection.error.PropertyTypeMismatchException;
import com.agileapes.couteau.reflection.property.impl.SimplePropertyDescriptor;
import com.agileapes.couteau.reflection.util.ReflectionUtils;
import com.agileapes.couteau.reflection.util.assets.SimpleParameterizedType;
import com.agileaps.couteau.freemarker.conversion.FreemarkerConversionStrategy;
import com.agileaps.couteau.freemarker.utils.FreemarkerUtils;
import freemarker.template.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This model is the focal point where the conversion framework is concentrated. Any beans converted through
 * this framework will be turned into instances of this class.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/15/13, 5:10 PM)
 */
public class GenericFreemarkerModel implements TemplateHashModelEx, BeanWrapper<GenericFreemarkerModel>, ConversionTarget {

    private Object bean;
    private BeanWrapper<Object> wrapper;
    private final Map<String, Object> values = new HashMap<String, Object>();
    private final Map<String, TemplateModel> models = new HashMap<String, TemplateModel>();
    private final FreemarkerConversionStrategy strategy = new FreemarkerConversionStrategy();

    @Override
    public boolean isWritable(String propertyName) throws NoSuchPropertyException {
        if (!hasProperty(propertyName)) {
            throw new NoSuchPropertyException(bean.getClass(), propertyName);
        }
        return true;
    }

    @Override
    public boolean isReadable(String propertyName) throws NoSuchPropertyException {
        if (!hasProperty(propertyName)) {
            throw new NoSuchPropertyException(bean.getClass(), propertyName);
        }
        return true;
    }

    @Override
    public Object getPropertyValue(String propertyName) throws NoSuchPropertyException, PropertyAccessException {
        if (!hasProperty(propertyName)) {
            throw new NoSuchPropertyException(bean.getClass(), propertyName);
        }
        return values.get(propertyName);
    }

    @Override
    public <P> P getPropertyValue(String propertyName, Class<P> propertyType) throws NoSuchPropertyException, PropertyAccessException {
        final Object propertyValue = getPropertyValue(propertyName);
        if (!propertyType.isInstance(propertyValue)) {
            throw new NoSuchPropertyException(bean.getClass(), propertyName, propertyType);
        }
        return propertyType.cast(propertyValue);
    }

    @Override
    public GenericFreemarkerModel getBean() {
        return this;
    }

    @Override
    public Class<GenericFreemarkerModel> getBeanType() {
        return GenericFreemarkerModel.class;
    }

    @Override
    public Set<String> getPropertyNames() {
        return values.keySet();
    }

    @Override
    public Class<?> getPropertyType(String propertyName) throws NoSuchPropertyException {
        final Class<?> propertyType = wrapper.getPropertyType(propertyName);
        if (Collection.class.isAssignableFrom(propertyType) || Map.class.isAssignableFrom(propertyType)) {
            return propertyType;
        }
        final ConversionDecision decision = strategy.decide(new SimplePropertyDescriptor(propertyName, propertyType, wrapper.getGenericPropertyType(propertyName)));
        if (ConversionDecision.CONVERT.equals(decision)) {
            return GenericFreemarkerModel.class;
        }
        return propertyType;
    }

    @Override
    public Type getGenericPropertyType(String propertyName) throws NoSuchPropertyException {
        final Type type = wrapper.getGenericPropertyType(propertyName);
        final Class<?> propertyType = wrapper.getPropertyType(propertyName);
        if (Collection.class.isAssignableFrom(propertyType) || Map.class.isAssignableFrom(propertyType)) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                final Class[] parameters = ReflectionUtils.resolveTypeArguments(type, Collection.class.isAssignableFrom(propertyType) ? 1 : 2);
                for (int i = 0; i < parameters.length; i++) {
                    Class parameter = parameters[i];
                    final ConversionDecision decision = strategy.decide(new SimplePropertyDescriptor("", parameter, parameter));
                    if (decision.equals(ConversionDecision.CONVERT)) {
                        parameter = GenericFreemarkerModel.class;
                    }
                    parameters[i] = parameter;
                }
                return new SimpleParameterizedType(parameterizedType.getRawType(), parameterizedType.getOwnerType(), parameters);
            }
        }
        return type;
    }

    @Override
    public boolean hasProperty(String propertyName) {
        return values.containsKey(propertyName) || wrapper.hasProperty(propertyName);
    }

    @Override
    public int size() throws TemplateModelException {
        return values.size();
    }

    @Override
    public TemplateCollectionModel keys() throws TemplateModelException {
        final ArrayList<TemplateModel> collection = new ArrayList<TemplateModel>();
        for (String key : models.keySet()) {
            collection.add(new SimpleScalar(key));
        }
        return new SimpleCollection(collection);
    }

    @Override
    public TemplateCollectionModel values() throws TemplateModelException {
        return new SimpleCollection(models.values());
    }

    @Override
    public TemplateModel get(String key) throws TemplateModelException {
        return models.get(key);
    }

    @Override
    public boolean isEmpty() throws TemplateModelException {
        return models.isEmpty();
    }

    @Override
    public void setBean(Object bean) {
        this.bean = bean;
        this.wrapper = new MethodBeanWrapper<Object>(bean);
    }

    @Override
    public void setPropertyValue(String propertyName, Object propertyValue) throws NoSuchPropertyException, PropertyAccessException, PropertyTypeMismatchException {
        if (!hasProperty(propertyName)) {
            throw new NoSuchPropertyException(bean.getClass(), propertyName);
        }
        values.put(propertyName, propertyValue);
        try {
            models.put(propertyName, FreemarkerUtils.convertItem(propertyValue));
        } catch (IllegalArgumentException e) {
            throw new PropertyTypeMismatchException(GenericFreemarkerModel.class, propertyName);
        }
    }

}
