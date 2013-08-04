<#-- @ftlvariable name="" type="com.agileapes.couteau.lang.model.JavaPojoModel" -->
<#if packageName != "">package ${packageName};</#if>

<#list imports as import>
import ${import};
</#list>
public class ${simpleName} {

<#list properties?keys as property>
    private ${properties[property]} ${property};
</#list>

<#list properties?keys as property>
<#assign type=properties[property]>
    public ${type} get${property?cap_first}() {
        return this.${property};
    }

    public void set${property?cap_first}(${type} ${property}) {
        this.${property} = ${property};
    }

</#list>

    @Override
    public String toString() {
        return "${simpleName} {" +
        <#list properties?keys as property>
            "${property}=" + get${property?cap_first}() +<#if property_has_next> ", " +</#if>
        </#list>
            "}";
    }
}