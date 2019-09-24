<#assign className = table.className>
<#assign tableRemarks = table.remarks?default("")>
<#assign hasDateTimeColumn = table.hasDateTimeColumn>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.web.process.${process};

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import ${basepackage}.dto.common.ExecuteResultDTO;
import ${basepackage}.dto.process.${process}.${className}DTO;
import ${basepackage}.dto.process.${process}.${className}QueryPageDTO;

/**
 * ${tableRemarks}
 * 
 * @author 01Studio
 */
@RequestMapping("/process/${process}/${classNameLower?lower_case}")
public interface I${className}Controller {

	<#if (table.pkColumn)??>
	@DeleteMapping(value = "/{${table.pkColumn.columnNameLower}:\\d+}")
	public void deleteByPrimaryKey(@PathVariable ${table.pkColumn.javaType} ${table.pkColumn.columnNameLower}) throws Exception;
	<#else>
	<#list table.columns as column>
    	<#if table.pkCount==0 && column_index==0>
	@DeleteMapping(value = "/{${column.columnNameLower}:\\d+}")
	public void deleteByPrimaryKey(@PathVariable ${column.javaType} ${column.columnNameLower}) throws Exception;
		</#if>
	</#list>
	</#if>
	
	@PostMapping
	public ExecuteResultDTO save(@RequestBody ${className}DTO ${classNameLower}DTO) throws Exception;
	
	<#if (table.pkColumn)??>
	@GetMapping(value = "/{${table.pkColumn.columnNameLower}:\\d+}")
	public ExecuteResultDTO findByPrimaryKey(@PathVariable ${table.pkColumn.javaType} ${table.pkColumn.columnNameLower}) throws Exception;
	<#else>
	<#list table.columns as column>
    	<#if table.pkCount==0 && column_index==0>
	@GetMapping(value = "/{${column.columnNameLower}:\\d+}")
	public ExecuteResultDTO findByPrimaryKey(@PathVariable ${column.javaType} ${column.columnNameLower}) throws Exception;
		</#if>
	</#list>
	</#if>
	
	@PutMapping
	public ExecuteResultDTO update(@RequestBody ${className}DTO ${classNameLower}DTO) throws Exception;
	
	@GetMapping
	public ExecuteResultDTO findByConds(${className}QueryPageDTO ${classNameLower}QueryPageDTO) throws Exception;
	
	@GetMapping(value = "/downExcel")
	public void downExcelByConds(${className}QueryPageDTO ${classNameLower}QueryPageDTO, HttpServletResponse response) throws Exception;

	@PostMapping(value = "/importExcel")
	public ExecuteResultDTO importExcel(MultipartFile file) throws Exception;
	
}