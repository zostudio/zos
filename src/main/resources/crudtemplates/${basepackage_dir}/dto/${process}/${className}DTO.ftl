<#assign className = table.className>
<#assign tableRemarks = table.remarks?default("暂无表注释")>
<#assign hasDateTimeColumn = table.hasDateTimeColumn>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.dto.${process};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ${tableRemarks}
 * 
 * @author 01Studio
 */
@ApiModel(description = "${tableRemarks}")
public class ${className}DTO {
	<@generateFields/>
	<@generatePkProperties/>
	<@generateNotPkProperties/>
}

<#macro generateFields>
	<#list table.columns as column>
	
	@ApiModelProperty(value = "${column.remarks?default("暂无数据注释[${column_index}]")}", example = <#if column.isStringColumn>"String"<#elseif column.isDateTimeColumn>"2000:01:01 00:00:00"<#elseif column.isNumberColumn>"0"<#else>"0"</#if>)
	private ${column.javaType} ${column.columnNameLower};
	</#list>
</#macro>

<#macro generatePkProperties>
	<#list table.columns as column>
		<#if column.pk>
	
	public void set${column.columnName}(${column.javaType} value) {
		this.${column.columnNameLower} = value;
	}
	
	public ${column.javaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
		</#if>
	</#list>
	</#macro>
	
	<#macro generateNotPkProperties>
	<#list table.columns as column>
		<#if !column.pk>
		    <#if table.pkCount==0 && column_index==0>
	
	public void set${column.columnName}(${column.javaType} value) {
		this.${column.columnNameLower} = value;
	}
	
	public ${column.javaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}	    
		    <#else>
	
	public ${column.javaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
	
	public void set${column.columnName}(${column.javaType} value) {
		this.${column.columnNameLower} = value;
	}
			</#if>
		</#if>
	</#list>
</#macro>