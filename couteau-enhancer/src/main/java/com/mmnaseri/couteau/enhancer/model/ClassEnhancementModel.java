/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
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

package com.mmnaseri.couteau.enhancer.model;

import freemarker.template.TemplateMethodModelEx;

/**
 * This is the model that is injected into the template to produce the enhanced class
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/9/10, 15:25)
 */
public class ClassEnhancementModel {

    private Class<?> superClass;
    private Class[] interfaces;
    private String enhancedName;
    private TemplateMethodModelEx isPublic = new PublicMethodFilter();
    private TemplateMethodModelEx isNotFinal = new NonFinalMethodFilter();
    private TemplateMethodModelEx mapType = new TypeMapMethod();
    private TemplateMethodModelEx valueOf = new AnnotationFieldGetterMethod();
    private TemplateMethodModelEx isNotImplemented = new NotImplementedMethod();
    private TemplateMethodModelEx isNotDeclared = new NotImplementedMethod();
    private TemplateMethodModelEx isNotStatic = new NonStaticMethodFilter();
    private TemplateMethodModelEx methodIndex = new MethodIndexMethod();
    private TemplateMethodModelEx pick = new MethodPickerMethod();

    public Class<?> getSuperClass() {
        return superClass;
    }

    public void setSuperClass(Class<?> superClass) {
        this.superClass = superClass;
    }

    public Class[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Class[] interfaces) {
        this.interfaces = interfaces;
    }

    public String getEnhancedName() {
        return enhancedName;
    }

    public void setEnhancedName(String enhancedName) {
        this.enhancedName = enhancedName;
    }

    public TemplateMethodModelEx getPublic() {
        return isPublic;
    }

    public TemplateMethodModelEx getNotFinal() {
        return isNotFinal;
    }

    public TemplateMethodModelEx getMapType() {
        return mapType;
    }

    public TemplateMethodModelEx getValueOf() {
        return valueOf;
    }

    public TemplateMethodModelEx getNotImplemented() {
        return isNotImplemented;
    }

    public TemplateMethodModelEx getMethodIndex() {
        return methodIndex;
    }

    public TemplateMethodModelEx getNotDeclared() {
        return isNotDeclared;
    }

    public TemplateMethodModelEx getNotStatic() {
        return isNotStatic;
    }

    public TemplateMethodModelEx getPick() {
        return pick;
    }
}
