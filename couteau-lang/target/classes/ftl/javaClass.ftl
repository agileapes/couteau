<#-- @ftlvariable name="" type="com.agileapes.couteau.lang.model.JavaClassModel" -->
<#if packageName != "">package ${packageName};</#if>

<#list imports as import>
import ${import};
</#list><#assign count = 0>
public class ${simpleName} {
<#list properties?keys as property>
    private ${properties[property]} ${property};<#if immutables[property] == true><#assign count=count + 1></#if>
</#list>
<#if count &gt; 0>
    public ${simpleName}(<#list properties?keys as property><#if immutables[property] == true><#assign type=properties[property]>${type} ${property}<#if property_has_next>, </#if></#if></#list>) {
    <#list properties?keys as property>
    <#if immutables[property] == true>
        this.${property} = ${property};
    </#if>
    </#list>
    }
</#if>
<#list properties?keys as property>
<#assign type=properties[property]>
    public ${type} get${property?cap_first}() {
        return this.${property};
    }
    <#if immutables[property] == false>
    public void set${property?cap_first}(${type} ${property}) {
        this.${property} = ${property};
    }
    </#if>

</#list>
<#list methods as method>
    ${method.visibility} ${method.returnType!"void"} ${method.name}(<#list method.parameters as parameter>${parameter.type} ${parameter.name}<#if parameter_has_next>, </#if></#list>) {
    <#list method.body as body>
    ${body}
    </#list>
    }

</#list>
}