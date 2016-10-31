/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Milad Naseri.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mmnaseri.couteau.context.spring;

import com.mmnaseri.couteau.context.contract.*;
import com.mmnaseri.couteau.context.error.RegistryException;
import com.mmnaseri.couteau.reflection.util.ClassUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>This class allows for configuring a Couteau context into a Spring context. The method to do this
 * is to first include this class as a bean inside a Spring application context and then fetch the
 * instance and ask the configurator to configure the context for you by calling to {@link #configure(Context)}</p>
 *
 * @see SpringConfigurableContext
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (6/29/13, 3:54 PM)
 */
public class SpringContextConfigurator implements BeanFactoryPostProcessor {

    private ConfigurableListableBeanFactory beanFactory;

    private final Set<Context<?>> contexts = new HashSet<Context<?>>();
    private final Set<Context<?>> autoConfigure = new HashSet<Context<?>>();

    public SpringContextConfigurator() {
    }

    /**
     * This constructor allows you to add contexts to be automatically configured by this configurator
     * @param contexts    the context being configured.
     */
    public SpringContextConfigurator(Collection<Context<?>> contexts) {
        autoConfigure.addAll(contexts);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        final Collection<SpringConfigurableContext> contexts = beanFactory.getBeansOfType(SpringConfigurableContext.class, false, true).values();
        for (SpringConfigurableContext context : contexts) {
            autoConfigure.add(context);
        }
        if (!autoConfigure.isEmpty()) {
            for (Context<?> context : autoConfigure) {
                ContextConfigurator<Context<?>> configurator = ContextConfigurator.NULL;
                if (context instanceof SpringConfigurableContext<?>) {
                    final SpringConfigurableContext<?> configurableContext = (SpringConfigurableContext<?>) context;
                    configurator = new ContextConfigurator<Context<?>>() {
                        @Override
                        public void configure(Context<?> context, ConfigurableListableBeanFactory beanFactory) {
                            configurableContext.configure(beanFactory);
                        }
                    };
                }
                try {
                    configure(context, configurator);
                } catch (RegistryException e) {
                    throw new FatalBeanException("Failed to configure context", e);
                }
            }
        }
    }

    /**
     * This will simply do the work that {@link #configure(Context, ContextConfigurator)} does
     * without the added option to configure the context manually once it has been configured by
     * this context.
     * @param context    th context to be configured
     * @param <C>        the type of the context
     * @throws RegistryException
     */
    public <C extends Context<?>> void configure(C context) throws RegistryException {
        configure(context, ContextConfigurator.NULL);
    }

    /**
     * This method will configure the context completely. What it does goes as follows:
     * <ul>
     *     <li>First of all, it will find bean factory aware contexts, and inject the contexts
     *     into them. This is so that if you have a context that has not been already initialized
     *     through Spring, and yet you would want to give it access to the Spring context, you would
     *     be able to. You can further proceed to call {@link ConfigurableListableBeanFactory#registerSingleton(String, Object)}
     *     to register this context with Spring.</li>
     *     <li>It will add any event listeners found in the Spring context and
     *     add it to the context being configured</li>
     *     <li>Then it will pick up bean processors that are applicable to this context, and add them as
     *     bean processors</li>
     *     <li>Next, it will do the same for context processors.</li>
     *     <li>It is at <em>this</em> stage that the configurator will be given a chance to manipulate
     *     the configuration process.</li>
     *     <li>After that, if at the steps above we have picked any context processors and the context
     *     is refreshable, we will refresh the context.</li>
     *     <li>Then we will find ContextAware beans that request access to this context, and give them
     *     to this context.</li>
     *     <li>Last but not least, we will take all the beans from this context, and register them as
     *     singleton objects with Spring. This allows Spring to separately configure them and inject any
     *     dependencies into them.</li>
     * </ul>
     * @param context         the context being configured
     * @param configurator    the configuration callback
     * @param <C>             the type of the context
     * @throws RegistryException
     */
    public <C extends Context<?>> void configure(C context, ContextConfigurator<C> configurator) throws RegistryException {
        if (contexts.contains(context)) {
            return;
        }
        contexts.add(context);
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
            if (context.getRegistryType().isAssignableFrom(processorType)) {
                //noinspection unchecked
                context.addBeanProcessor(processor);
            }
        }
        boolean needsRefresh = false;
        final Map<String, ContextProcessor> contextProcessors = beanFactory.getBeansOfType(ContextProcessor.class, false, true);
        for (ContextProcessor processor : contextProcessors.values()) {
            final Class<?> processorType = ClassUtils.resolveTypeArgument(processor.getClass(), ContextProcessor.class);
            if (context.getRegistryType().isAssignableFrom(processorType)) {
                needsRefresh = true;
                //noinspection unchecked
                context.addContextProcessor(processor);
            }
        }
        configurator.configure(context, beanFactory);
        if (needsRefresh) {
            if (context instanceof ReconfigurableContext<?>) {
                ((ReconfigurableContext<?>) context).refresh();
            }
        }
        final Map<String, ContextAware> contextAwareBeans = beanFactory.getBeansOfType(ContextAware.class, false, true);
        for (ContextAware bean : contextAwareBeans.values()) {
            final Class<?> contextType = ClassUtils.resolveTypeArgument(bean.getClass(), ContextAware.class);
            if (contextType.isAssignableFrom(context.getRegistryType())) {
                //noinspection unchecked
                bean.setContext(context);
            }
        }
        for (String beanName : context.getBeanNames()) {
            beanFactory.registerSingleton(context.getClass().getCanonicalName().concat(".").concat(beanName), context.get(beanName));
        }
    }

}
