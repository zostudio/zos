<#assign className = table.className>
<#assign tableRemarks = table.remarks?default("暂无表注释")>
<#assign hasDateTimeColumn = table.hasDateTimeColumn>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.excel.model;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * ${tableRemarks}
 * 
 * @author 01Studio
 */
public class ${className}Excel implements Serializable {
	<@generateFields/>
	<@generatePkProperties/>
	<@generateNotPkProperties/>
}

<#macro generateFields>
	<#list table.columns as column>
		<#if column.pk>
	
	// @Excel(name = "${column.remarks?default("暂无数据注释[${column_index}]")}", orderNum = "${column_index}", width=18)
	private ${column.javaType} ${column.columnNameLower};
		</#if>
		<#if !column.pk>
	
	@Excel(name = "${column.remarks?default("暂无数据注释[${column_index}]")}", orderNum = "${column_index - 1}", width=18)
	private ${column.javaType} ${column.columnNameLower};
		</#if>
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