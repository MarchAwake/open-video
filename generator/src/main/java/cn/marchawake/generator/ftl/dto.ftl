package cn.marchawake.server.dto;

<#list typeSet as type>
    <#if type=='Date'>
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
    </#if>
    <#if type=='BigDecimal'>
        import java.math.BigDecimal;
    </#if>
</#list>
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ${Domain}Dto {

<#list fieldList as field>

    /**
    * ${field.comment}
    */
    <#if field.javaType=='Date'>
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    </#if>
    private ${field.javaType} ${field.nameHump};

</#list>

<#--<#list fieldList as field>-->
<#--    public ${field.javaType} get${field.nameBigHump}() {-->
<#--    return ${field.nameHump};-->
<#--    }-->

<#--    public void set${field.nameBigHump}(${field.javaType} ${field.nameHump}) {-->
<#--    this.${field.nameHump} = ${field.nameHump};-->
<#--    }-->

<#--</#list>-->

}