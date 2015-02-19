<#-- @ftlvariable name="" type="com.agileapes.couteau.enhancer.model.ClassEnhancementModel" -->
package ${superClass.canonicalName?substring(0, superClass.canonicalName?last_index_of('.'))};
<#assign simpleName = enhancedName?substring(enhancedName?last_index_of(".") + 1)/>

import com.agileapes.couteau.enhancer.api.MethodInterceptor;
import com.agileapes.couteau.enhancer.api.Interceptible;
import com.agileapes.couteau.enhancer.impl.NoOpInterceptor;
import com.agileapes.couteau.enhancer.error.InvocationTargetError;
import com.agileapes.couteau.enhancer.api.MethodProxy;
import com.agileapes.couteau.enhancer.error.NoImplementationFoundForMethodError;
import com.agileapes.couteau.enhancer.impl.ImmutableMethodDescriptor;
import com.agileapes.couteau.enhancer.api.MethodDescriptor;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class ${simpleName} extends ${superClass.canonicalName} implements Interceptible<#if interfaces?size &gt; 0>, </#if><#list interfaces as interface>${interface.canonicalName}<#if interface_has_next>, </#if></#list> {

    private static final Map<Integer, MethodDescriptor> methodDescriptors = new HashMap<Integer, MethodDescriptor>();
    static {
        <#list notDeclared(notFinal(public(notStatic(pick(superClass.methods))))) as method>
        methodDescriptors.put(${methodIndex(method)}, new ImmutableMethodDescriptor(${superClass.canonicalName}.class, ${method.returnType.canonicalName}.class, "${method.name}", new Class[]{<#list method.parameterTypes as parameterType>${parameterType.canonicalName}.class<#if parameterType_has_next>, </#if></#list>}, Arrays.<Annotation>asList(<#list method.annotations as annotation>
        new ${annotation.annotationType().canonicalName}() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return ${annotation.annotationType().canonicalName}.class;
            }

            <#list annotation.annotationType().declaredMethods as field>
            @Override
            public ${field.returnType.canonicalName} ${field.name}() {
                return ${valueOf(annotation, field.name)};
            }
            </#list>
        }<#if annotation_has_next>,</#if>
        </#list>).toArray(new Annotation[${method.annotations?size}])));
        </#list>
        <#list interfaces as interface>
        <#list notDeclared(interface.methods) as method>
        methodDescriptors.put(${methodIndex(method)}, new ImmutableMethodDescriptor(${interface.canonicalName}.class, ${method.returnType.canonicalName}.class, "${method.name}", new Class[]{<#list method.parameterTypes as parameterType>${parameterType.canonicalName}.class<#if parameterType_has_next>, </#if></#list>}, Arrays.<Annotation>asList(<#list method.annotations as annotation>
        new ${annotation.annotationType().canonicalName}() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return ${annotation.annotationType().canonicalName}.class;
            }

            <#list annotation.annotationType().declaredMethods as field>
            @Override
            public ${field.returnType.canonicalName} ${field.name}() {
                return ${valueOf(annotation, field.name)};
            }
            </#list>
        }<#if annotation_has_next>,</#if>
        </#list>).toArray(new Annotation[${method.annotations?size}])));
        </#list>
        </#list>
    }

    private MethodInterceptor interceptor;

<#list superClass.constructors as constructor>
    public ${simpleName}(<#list constructor.parameterTypes as parameterType>${parameterType.canonicalName} param${parameterType_index}<#if parameterType_has_next>, </#if></#list>) {
        super(<#list constructor.parameterTypes as parameterType>param${parameterType_index}<#if parameterType_has_next>, </#if></#list>);
        setInterceptor(null);
    }

</#list>

    @Override
    public synchronized void setInterceptor(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
        if (this.interceptor == null) {
            this.interceptor = new NoOpInterceptor();
        }
    }

<#list notImplemented(notFinal(public(notStatic(pick(superClass.methods))))) as method>
    @Override
    public ${method.returnType.canonicalName} ${method.name}(<#list method.parameterTypes as parameterType>final ${parameterType.canonicalName} param${parameterType_index}<#if parameterType_has_next>, </#if></#list>)<#if method.exceptionTypes?size &gt; 0> throws <#list method.exceptionTypes as exceptionType>${exceptionType.canonicalName}<#if exceptionType_has_next>, </#if></#list></#if> {
        final MethodDescriptor method = methodDescriptors.get(${methodIndex(method)});
        final Object result;
        try {
            result = interceptor.intercept(method, this, new Object[]{<#list method.parameterTypes as parameterType>param${parameterType_index}<#if parameterType_has_next>, </#if></#list>}, new MethodProxy() {
                @Override
                public Object callSuper(Object target, Object[] arguments) throws Throwable {
                    Object result<#if method.returnType.canonicalName == "void"> = null</#if>;
                    <#if method.returnType.canonicalName != "void">result = </#if>${simpleName}.super.${method.name}(<#list method.parameterTypes as parameterType>param${parameterType_index}<#if parameterType_has_next>, </#if></#list>);
                    return result;
                }
            });
        } catch (Throwable throwable) {
            throw new InvocationTargetError(method, throwable);
        }
        <#if method.returnType.canonicalName != "void">return (${mapType(method.returnType).canonicalName}) result;</#if>
    }

</#list>
<#list interfaces as interface>
    <#list notImplemented(interface.methods) as method>
    @Override
    public ${method.returnType.canonicalName} ${method.name}(<#list method.parameterTypes as parameterType>final ${parameterType.canonicalName} param${parameterType_index}<#if parameterType_has_next>, </#if></#list>)<#if method.exceptionTypes?size &gt; 0> throws <#list method.exceptionTypes as exceptionType>${exceptionType.canonicalName}<#if exceptionType_has_next>, </#if></#list></#if> {
        final MethodDescriptor method = methodDescriptors.get(${methodIndex(method)});
        final Object result;
        try {
            result = interceptor.intercept(method, this, new Object[]{<#list method.parameterTypes as parameterType>param${parameterType_index}<#if parameterType_has_next>, </#if></#list>}, new MethodProxy() {
                @Override
                public Object callSuper(Object target, Object[] arguments) throws Throwable {
                    throw new NoImplementationFoundForMethodError(${interface.canonicalName}.class, "${method.name}");
                }
            });
        } catch (Throwable throwable) {
            throw new InvocationTargetError(method, throwable);
        }
        <#if method.returnType.canonicalName != "void">return (${mapType(method.returnType).canonicalName}) result;</#if>
    }
    </#list>
</#list>
}