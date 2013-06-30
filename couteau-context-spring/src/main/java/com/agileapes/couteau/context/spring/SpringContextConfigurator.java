package com.agileapes.couteau.context.spring;

import com.agileapes.couteau.context.contract.*;
import com.agileapes.couteau.context.error.RegistryException;
import com.agileapes.couteau.context.util.ClassUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/29/13, 3:54 PM)
 */
public class SpringContextConfigurator implements BeanFactoryPostProcessor {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public <C extends Context<?>> void configure(C context) throws RegistryException {
        configure(context, null);
    }

    public <C extends Context<?>> void configure(C context, ContextConfigurator<C> configurator) throws RegistryException {
        if (context instanceof BeanFactoryAwareContext) {
            ((BeanFactoryAwareContext) context).setBeanFactory(beanFactory);
        }
        final Map<String,EventListener> listeners = beanFactory.getBeansOfType(EventListener.class, false, true);
        for (EventListener<?> eventListener : listeners.values()) {
            context.addEventListener(eventListener);
        }
        final Map<String, BeanProcessor> processors = beanFactory.getBeansOfType(BeanProcessor.class, false, true);
        for (BeanProcessor processor : processors.values()) {
            final Class<?> processorType = ClassUtils.resolveTypeArgument(processor.getClass(), BeanProcessor.class);
            if (context.getContextType().isAssignableFrom(processorType)) {
                //noinspection unchecked
                context.addBeanProcessor(processor);
            }
        }
        boolean needsRefresh = false;
        final Map<String, ContextProcessor> contextProcessors = beanFactory.getBeansOfType(ContextProcessor.class, false, true);
        for (ContextProcessor processor : contextProcessors.values()) {
            final Class<?> processorType = ClassUtils.resolveTypeArgument(processor.getClass(), ContextProcessor.class);
            if (context.getContextType().isAssignableFrom(processorType)) {
                needsRefresh = true;
                //noinspection unchecked
                context.addContextProcessor(processor);
            }
        }
        if (configurator != null) {
            configurator.configure(context, beanFactory);
        }
        if (needsRefresh) {
            if (context instanceof ReconfigurableContext<?>) {
                ((ReconfigurableContext<?>) context).refresh();
            }
        }
        final Map<String, ContextAware> contextAwareBeans = beanFactory.getBeansOfType(ContextAware.class, false, true);
        for (ContextAware bean : contextAwareBeans.values()) {
            final Class<?> contextType = ClassUtils.resolveTypeArgument(bean.getClass(), ContextAware.class);
            if (contextType.isAssignableFrom(context.getContextType())) {
                //noinspection unchecked
                bean.setContext(context);
            }
        }
        for (String beanName : context.getBeanNames()) {
            beanFactory.registerSingleton(context.getClass().getCanonicalName().concat(".").concat(beanName), context.get(beanName));
        }
    }

}
