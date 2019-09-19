<#assign className = table.className>
<#assign tableRemarks = table.remarks?default("")>
<#assign hasDateTimeColumn = table.hasDateTimeColumn>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.service.process.${process};

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.asiainfo.rms.core.api.Page;
import ${basepackage}.bo.process.${process}.${className}BO;
import ${basepackage}.bo.process.${process}.${className}QueryPageBO;
import ${basepackage}.dto.process.${process}.${className}DTO;

/**
 * ${tableRemarks}
 * 
 * @author 01Studio
 */
public interface I${className}Service {

	<#if (table.pkColumn)??>
	public void deleteByPrimaryKey(${table.pkColumn.javaType} ${table.pkColumn.columnNameLower}) throws Exception;
	<#else>
	<#list table.columns as column>
    	<#if table.pkCount==0 && column_index==0>
	public void deleteByPrimaryKey(${column.javaType} ${column.columnNameLower}) throws Exception;
		</#if>
	</#list>
	</#if>
	
	public ${className}BO save(${className}BO ${classNameLower}BO) throws Exception;
	
	<#if (table.pkColumn)??>
	public ${className}BO findByPrimaryKey(${table.pkColumn.javaType} ${table.pkColumn.columnNameLower}) throws Exception;
	<#else>
	<#list table.columns as column>
    	<#if table.pkCount==0 && column_index==0>
	public ${className}BO findByPrimaryKey(${column.javaType} ${column.columnNameLower}) throws Exception;
		</#if>
	</#list>
	</#if>
	
	public ${className}BO update(${className}BO ${classNameLower}BO) throws Exception;
	
	public Page<${className}BO> findByConds(${className}QueryPageBO ${classNameLower}QueryPageBO) throws Exception;
	
	public boolean verify(${className}BO bo, StringBuffer errorMsg) throws Exception;
	
	public boolean importExcel(MultipartFile file, List<${className}DTO> successList,List<${className}DTO> errorList, StringBuffer errorMsg);

	public void downExcelByConds(${className}QueryPageBO ${classNameLower}QueryPageBO, HttpServletResponse response) throws Exception;
	
	public boolean receiveExcel(File file, List<${className}BO> successList, List<${className}BO> errorList, StringBuffer errorMsg) throws Exception;
	
}