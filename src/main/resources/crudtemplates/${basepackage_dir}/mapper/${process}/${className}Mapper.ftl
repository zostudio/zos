<#assign className = table.className>
<#assign tableRemarks = table.remarks?default("暂无表注释")>
<#assign hasDateTimeColumn = table.hasDateTimeColumn>
<#assign dateTimeColumnCount = table.dateTimeColumnCount>
<#assign classNameLower = className?uncap_first>
<#assign outputDateTimeColumnCount = 0>
package ${basepackage}.mapper.${process};

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.asiainfo.rms.core.api.Page;
import ${basepackage}.bo.process.${process}.${className}BO;
import ${basepackage}.bo.process.${process}.${className}QueryPageBO;
import ${basepackage}.domain.${process}.BO${className};
import ${basepackage}.dto.process.${process}.${className}DTO;
import ${basepackage}.dto.process.${process}.${className}QueryPageDTO;
import ${basepackage}.excel.model.${className}Excel;

/**
 * ${tableRemarks}
 * 
 * @author 01Studio
 */
@Mapper
public interface ${className}Mapper {
	
	${className}Mapper INSTANCE = Mappers.getMapper(${className}Mapper.class);

	<#if hasDateTimeColumn>
	@Mappings({
		<#assign outputDateTimeColumnCount = 0>
		<#list table.columns as column>
			<#if column.isDateTimeColumn>
			<#assign outputDateTimeColumnCount = outputDateTimeColumnCount + 1>
        @Mapping(source = "${column.columnNameLower}", target = "${column.columnNameLower}", dateFormat = "yyyy-MM-dd HH:mm:ss")<#if (dateTimeColumnCount>outputDateTimeColumnCount)>,</#if>
        	</#if>
        </#list>
	})
	</#if>
	public ${className}DTO boToDto(${className}BO ${classNameLower}BO);
	
	public List<${className}DTO> boToDto(List<${className}BO> ${classNameLower}BOs);
	
	public Page<${className}DTO> boToDto(Page<${className}BO> ${classNameLower}BOs);

	@Mappings({
		<#list table.columns as column>
			<#if column.isDateTimeColumn>
		@Mapping(source = "${column.sqlName?lower_case}_begin", target = "${column.columnNameLower}Begin", dateFormat = "yyyy-MM-dd HH:mm:ss"),
		@Mapping(source = "${column.sqlName?lower_case}_end", target = "${column.columnNameLower}End", dateFormat = "yyyy-MM-dd HH:mm:ss"),
			<#elseif column.isNumberColumn>
		@Mapping(source = "${column.sqlName?lower_case}_begin", target = "${column.columnNameLower}Begin"),
		@Mapping(source = "${column.sqlName?lower_case}_end", target = "${column.columnNameLower}End"),
			<#else>
		@Mapping(source = "${column.sqlName?lower_case}", target = "${column.columnNameLower}"),
			</#if>
		</#list>
        @Mapping(source = "page_no", target = "pageNo"),
        @Mapping(source = "page_size", target = "pageSize")
    })
	public ${className}QueryPageBO dtoToBo(${className}QueryPageDTO ${classNameLower}QueryPageDTO);
	
	<#if hasDateTimeColumn>
	@Mappings({
		<#assign outputDateTimeColumnCount = 0>
		<#list table.columns as column>
			<#if column.isDateTimeColumn>
			<#assign outputDateTimeColumnCount = outputDateTimeColumnCount + 1>
        @Mapping(source = "${column.columnNameLower}", target = "${column.columnNameLower}", dateFormat = "yyyy-MM-dd HH:mm:ss")<#if (dateTimeColumnCount>outputDateTimeColumnCount)>,</#if>
        	</#if>
        </#list>
	})
	</#if>
	public ${className}BO dtoToBo(${className}DTO ${classNameLower}DTO);

	<#if hasDateTimeColumn>
	@Mappings({
		<#assign outputDateTimeColumnCount = 0>
		<#list table.columns as column>
			<#if column.isDateTimeColumn>
			<#assign outputDateTimeColumnCount = outputDateTimeColumnCount + 1>
        @Mapping(source = "${column.columnNameLower}", target = "${column.columnNameLower}", dateFormat = "yyyy-MM-dd HH:mm:ss")<#if (dateTimeColumnCount>outputDateTimeColumnCount)>,</#if>
        	</#if>
        </#list>
	})
	</#if>
	public BO${className} boToDomain(${className}BO ${classNameLower}BO);

	<#if hasDateTimeColumn>
	@Mappings({
		<#assign outputDateTimeColumnCount = 0>
		<#list table.columns as column>
			<#if column.isDateTimeColumn>
			<#assign outputDateTimeColumnCount = outputDateTimeColumnCount + 1>
        @Mapping(source = "${column.columnNameLower}", target = "${column.columnNameLower}", dateFormat = "yyyy-MM-dd HH:mm:ss")<#if (dateTimeColumnCount>outputDateTimeColumnCount)>,</#if>
        	</#if>
        </#list>
	})
	</#if>
	public ${className}BO domainToBo(BO${className} bo${className});
	
	public List<${className}BO> domainToBo(List<BO${className}> bo${className}s);
	
	public Page<${className}BO> domainToBo(Page<BO${className}> bo${className}s);
	
	public ${className}Excel boToExcel(${className}BO ${classNameLower}BO);
	
	public List<${className}Excel> boToExcel(List<${className}BO> ${classNameLower}BOs);
	
	public ${className}BO excelToBo(${className}Excel ${classNameLower}Excel);
	
	public List<${className}BO> excelToBo(List<${className}Excel> ${classNameLower}Excels);
}