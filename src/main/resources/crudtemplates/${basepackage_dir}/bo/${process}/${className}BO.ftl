<#assign className = table.className>
<#assign tableRemarks = table.remarks?default("暂无表注释")>
<#assign hasDateTimeColumn = table.hasDateTimeColumn>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.bo.${process};

/**
 * ${tableRemarks}
 * 
 * @author 01Studio
 */
public class ${className}BO {
	<@generateFields/>
	<@generatePkProperties/>
	<@generateNotPkProperties/>
}

<#macro generateFields>
	<#list table.columns as column>
	
	/**
	 * ${column.remarks?default("暂无数据注释(${column_index})")}
	 */
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