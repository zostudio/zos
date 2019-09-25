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
public class ${className}QueryPageDTO {
	<@generateFields/>
	
	@ApiModelProperty(value = "分页页码", example = "1")
	private Integer page_no;
	
	@ApiModelProperty(value = "分页步长", example = "20")
	private Integer page_size;
	<@generatePkProperties/>
	<@generateNotPkProperties/>
	
	public Integer getPage_no() {
		return this.page_no;
	}
	
	public Integer getPage_size() {
		return this.page_size;
	}
	
	public void setPage_no(Integer page_no) {
		this.page_no = page_no;
	}
	
	public void setPage_size(Integer page_size) {
		this.page_size = page_size;
	}
}

<#macro generateFields>
	<#list table.columns as column>
	<#if column.isDateTimeColumn>
	
	@ApiModelProperty(value = "${column.remarks?default("暂无数据注释[${column_index}]")}(开始)", example = <#if column.isStringColumn>"String"<#elseif column.isDateTimeColumn>"2000:01:01 00:00:00"<#elseif column.isNumberColumn>"0"<#else>"0"</#if>)
	private String ${column.sqlName?lower_case}_begin;
	
	@ApiModelProperty(value = "${column.remarks?default("暂无数据注释[${column_index}]")}(结束)", example = <#if column.isStringColumn>"String"<#elseif column.isDateTimeColumn>"2000:01:01 00:00:00"<#elseif column.isNumberColumn>"0"<#else>"0"</#if>)
	private String ${column.sqlName?lower_case}_end;
	<#elseif column.isNumberColumn>
	
	@ApiModelProperty(value = "${column.remarks?default("暂无数据注释[${column_index}]")}(开始)", example = <#if column.isStringColumn>"String"<#elseif column.isDateTimeColumn>"2000:01:01 00:00:00"<#elseif column.isNumberColumn>"0"<#else>"0"</#if>)
	private ${column.javaType} ${column.sqlName?lower_case}_begin;
	
	@ApiModelProperty(value = "${column.remarks?default("暂无数据注释[${column_index}]")}(结束)", example = <#if column.isStringColumn>"String"<#elseif column.isDateTimeColumn>"2000:01:01 00:00:00"<#elseif column.isNumberColumn>"0"<#else>"0"</#if>)
	private ${column.javaType} ${column.sqlName?lower_case}_end;
	<#else>
	
	@ApiModelProperty(value = "${column.remarks?default("暂无数据注释[${column_index}]")}", example = <#if column.isStringColumn>"String"<#elseif column.isDateTimeColumn>"2000:01:01 00:00:00"<#elseif column.isNumberColumn>"0"<#else>"0"</#if>)
	private ${column.javaType} ${column.sqlName?lower_case};
	</#if>
	</#list>
</#macro>

<#macro generatePkProperties>
	<#list table.columns as column>
		<#if column.pk && column.isNumberColumn>
	
	public ${column.javaType} get${column.sqlName?capitalize}_begin() {
		return this.${column.sqlName?lower_case}_begin;
	}
	
	public void set${column.sqlName?capitalize}_begin(${column.javaType} value) {
		this.${column.sqlName?lower_case}_begin = value;
	}
	
	public ${column.javaType} get${column.sqlName?capitalize}_end() {
		return this.${column.sqlName?lower_case}_end;
	}
	
	public void set${column.sqlName?capitalize}_end(${column.javaType} value) {
		this.${column.sqlName?lower_case}_end = value;
	}
		<#elseif column.pk>
	
	public ${column.javaType} get${column.sqlName?capitalize}() {
		return this.${column.sqlName?lower_case};
	}

	public void set${column.sqlName?capitalize}(${column.javaType} value) {
		this.${column.sqlName?lower_case} = value;
	}	
		</#if>
	</#list>
</#macro>

<#macro generateNotPkProperties>
	<#list table.columns as column>
		<#if !column.pk>
		    <#if table.pkCount==0 && column_index==0>
		    	<#if column.isNumberColumn>
	public ${column.javaType} get${column.sqlName?capitalize}_begin() {
		return this.${column.sqlName?lower_case}_begin;
	}
	
	public void set${column.sqlName?capitalize}_begin(${column.javaType} value) {
		this.${column.sqlName?lower_case}_begin = value;
	}
	
	public ${column.javaType} get${column.sqlName?capitalize}_end() {
		return this.${column.sqlName?lower_case}_end;
	}
	
	public void set${column.sqlName?capitalize}_end(${column.javaType} value) {
		this.${column.sqlName?lower_case}_end = value;
	}
				<#else>
	public ${column.javaType} get${column.sqlName?capitalize}() {
		return this.${column.sqlName?lower_case};
	}

	public void set${column.sqlName?capitalize}(${column.javaType} value) {
		this.${column.sqlName?lower_case} = value;
	}	    	
		    	</#if>
			<#else>
		    	<#if column.isNumberColumn>
	public ${column.javaType} get${column.sqlName?capitalize}_begin() {
		return this.${column.sqlName?lower_case}_begin;
	}
	
	public void set${column.sqlName?capitalize}_begin(${column.javaType} value) {
		this.${column.sqlName?lower_case}_begin = value;
	}
	
	public ${column.javaType} get${column.sqlName?capitalize}_end() {
		return this.${column.sqlName?lower_case}_end;
	}
	
	public void set${column.sqlName?capitalize}_end(${column.javaType} value) {
		this.${column.sqlName?lower_case}_end = value;
	}
				<#elseif column.isDateTimeColumn>

	public String get${column.sqlName?capitalize}_begin() {
		return this.${column.sqlName?lower_case}_begin;
	}
	
	public void set${column.sqlName?capitalize}_begin(String value) {
		this.${column.sqlName?lower_case}_begin = value;
	}
	
	public String get${column.sqlName?capitalize}_end() {
		return this.${column.sqlName?lower_case}_end;
	}
	
	public void set${column.sqlName?capitalize}_end(String value) {
		this.${column.sqlName?lower_case}_end = value;
	}
				<#else>
	public ${column.javaType} get${column.sqlName?capitalize}() {
		return this.${column.sqlName?lower_case};
	}

	public void set${column.sqlName?capitalize}(${column.javaType} value) {
		this.${column.sqlName?lower_case} = value;
	}	    	
		    	</#if>
			</#if>
		</#if>
	</#list>
</#macro>