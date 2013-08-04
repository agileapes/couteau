<#-- @ftlvariable name="" type="com.agileapes.couteau.lang.model.JavaClassModel" -->
<#if packageName != "">package ${packageName};</#if>

<#list imports as import>
import ${import};
</#list>

public class ${simpleName} {
<#list properties?keys as property>
    private final ${properties[property]} ${property};
</#list>

    public ${simpleName}(<#list properties?keys as property><#assign type=properties[property]>${type} ${property}<#if property_has_next>, </#if></#list>) {
    <#list properties?keys as property>
        this.${property} = ${property};
    </#list>
    }
<#list properties?keys as property>
<#assign type=properties[property]>
    public ${type} get${property?cap_first}() {
        return this.${property};
    }

</#list>
}