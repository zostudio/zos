<#assign className = table.className>
<#assign tableRemarks = table.remarks?default("")>
<#assign hasDateTimeColumn = table.hasDateTimeColumn>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.service.process.${process}.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.asiainfo.rms.core.api.Page;
import ${basepackage}.bo.process.${process}.${className}BO;
import ${basepackage}.bo.process.${process}.${className}QueryPageBO;
import ${basepackage}.dao.CommonDAO;
import ${basepackage}.domain.${process}.BO${className};
import ${basepackage}.dto.process.${process}.${className}DTO;
import ${basepackage}.excel.model.${className}Excel;
import com.asiainfo.rms.workflow.excel.util.GenerateExport;
import ${basepackage}.mapper.process.${process}.${className}Mapper;
import ${basepackage}.service.process.${process}.I${className}Service;

/**
 * ${tableRemarks}
 * 
 * @author 01Studio
 */
@Service("${classNameLower}Service")
@Transactional(rollbackOn = Exception.class)
public class ${className}ServiceImpl implements I${className}Service {
	
	@Autowired
	private CommonDAO commonDAO;
	
	@Override
	<#if (table.pkColumn)??>
	public void deleteByPrimaryKey(${table.pkColumn.javaType} ${table.pkColumn.columnNameLower}) throws Exception {
		commonDAO.delete(${table.pkColumn.columnNameLower}, BO${className}.class);
	<#else>
	<#list table.columns as column>
    	<#if table.pkCount==0 && column_index==0>
	public void deleteByPrimaryKey(${column.javaType} ${column.columnNameLower}) throws Exception {
		commonDAO.delete(${column.columnNameLower}, BO${className}.class);
		</#if>
	</#list>
	</#if>
	}
	
	@Override
	public ${className}BO save(${className}BO ${classNameLower}BO) throws Exception {
		BO${className} bo${className} = ${className}Mapper.INSTANCE.boToDomain(${classNameLower}BO);
		bo${className} = commonDAO.saveOrUpdate(bo${className}, BO${className}.class);
		return ${className}Mapper.INSTANCE.domainToBo(bo${className});
	}
	
	@Override
	<#if (table.pkColumn)??>
	public ${className}BO findByPrimaryKey(${table.pkColumn.javaType} ${table.pkColumn.columnNameLower}) throws Exception{
		BO${className} bo${className} = commonDAO.findById(BO${className}.class, ${table.pkColumn.columnNameLower});
	<#else>
	<#list table.columns as column>
    	<#if table.pkCount==0 && column_index==0>
	public ${className}BO findByPrimaryKey(${column.javaType} ${column.columnNameLower}) throws Exception {
		BO${className} bo${className} = commonDAO.findById(BO${className}.class, ${column.columnNameLower});
		</#if>
	</#list>
	</#if>
		return ${className}Mapper.INSTANCE.domainToBo(bo${className});
	}
	
	@Override
	public ${className}BO update(${className}BO ${classNameLower}BO) throws Exception {
		BO${className} bo${className} = ${className}Mapper.INSTANCE.boToDomain(${classNameLower}BO);
		StringBuffer hqlCondition = new StringBuffer("UPDATE ${table.sqlName} SET ");
		Map<String, Object> param = new HashMap<String, Object>();
		<#list table.columns as column>
		    <#if column.isNumberColumn>
	    if (${classNameLower}BO.get${column.columnName}() != null) {
			hqlCondition.append(" ${column.sqlName} = :${column.columnNameLower}, ");
			param.put("${column.columnNameLower}", ${classNameLower}BO.get${column.columnName}());
		}
	    	<#elseif column.isDateTimeColumn>
    	java.text.SimpleDateFormat sf${column.columnName} = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    if (${classNameLower}BO.get${column.columnName}() != null) {
			hqlCondition.append(" ${column.sqlName} = to_date('");
			hqlCondition.append(sf${column.columnName}.format(${classNameLower}BO.get${column.columnName}()));
			hqlCondition.append("', 'yyyy-MM-dd HH24:mi:ss'), ");
		}
		    <#elseif column.isStringColumn>
	    if (StringUtils.isNotEmpty(${classNameLower}BO.get${column.columnName}())) {
			hqlCondition.append(" ${column.sqlName} = :${column.columnNameLower}, ");
			param.put("${column.columnNameLower}", ${classNameLower}BO.get${column.columnName}());
		}
	    	<#else>
	    if (${classNameLower}QueryPageBO.get${column.columnName}() != null) {
			hqlCondition.append(" ${column.sqlName} = :${column.columnNameLower}, ");
			param.put("${column.columnNameLower}", ${classNameLower}BO.get${column.columnName}());
		}
			</#if>
		</#list>
		hqlCondition = hqlCondition.replace(sql.toString().lastIndexOf(","), hqlCondition.toString().lastIndexOf(",") + 1, "");
		<#if (table.pkColumn)??>
		hqlCondition.append(" WHERE ${table.pkColumn.sqlName}=" + ${classNameLower}BO.get${table.pkColumn.columnName}());
		<#else>
		<#list table.columns as column>
	    	<#if table.pkCount==0 && column_index==0>
		hqlCondition.append(" WHERE ${column.sqlName}=" + ${classNameLower}BO.get${column.columnName}());
			</#if>
		</#list>
		</#if>
		commonDAO.updateBySql(hqlCondition.toString(), param);
		<#if (table.pkColumn)??>
		bo${className} = commonDAO.findById(BO${className}.class, ${classNameLower}BO.get${table.pkColumn.columnName}());
		<#else>
		<#list table.columns as column>
	    	<#if table.pkCount==0 && column_index==0>
		bo${className} = commonDAO.findById(BO${className}.class, ${classNameLower}BO.get${column.columnName}());
			</#if>
		</#list>
		</#if>
		return ${className}Mapper.INSTANCE.domainToBo(bo${className});
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Page<${className}BO> findByConds(${className}QueryPageBO ${classNameLower}QueryPageBO) throws Exception {
		StringBuffer hql = new StringBuffer("SELECT o FROM BO${className} o WHERE 1 = 1");
		StringBuffer hqlCount = new StringBuffer("SELECT COUNT(o) FROM BO${className} o WHERE 1 = 1");
		StringBuffer hqlCondition = new StringBuffer();
		Map<String, Object> param = new HashMap<String, Object>();
		<#list table.columns as column>
		    <#if column.isNumberColumn>
	    if (${classNameLower}QueryPageBO.get${column.columnName}Begin() != null) {
			hqlCondition.append(" AND o.${column.columnNameLower} >= :${column.columnNameLower}Begin");
			param.put("${column.columnNameLower}Begin", ${classNameLower}QueryPageBO.get${column.columnName}Begin());
		}
	    if (${classNameLower}QueryPageBO.get${column.columnName}End() != null) {
			hqlCondition.append(" AND o.${column.columnNameLower} <= :${column.columnNameLower}End");
			param.put("${column.columnNameLower}End", ${classNameLower}QueryPageBO.get${column.columnName}End());
		}
	    	<#elseif column.isDateTimeColumn>
    	java.text.SimpleDateFormat sf${column.columnName} = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    if (${classNameLower}QueryPageBO.get${column.columnName}Begin() != null) {
			hqlCondition.append(" AND o.${column.columnNameLower} >= to_date('");
			hqlCondition.append(sf${column.columnName}.format(${classNameLower}QueryPageBO.get${column.columnName}Begin()));
			hqlCondition.append("', 'yyyy-MM-dd HH24:mi:ss')");
		}
	    if (${classNameLower}QueryPageBO.get${column.columnName}End() != null) {
			hqlCondition.append(" AND o.${column.columnNameLower} <= to_date('");
			hqlCondition.append(sf${column.columnName}.format(${classNameLower}QueryPageBO.get${column.columnName}End()));
			hqlCondition.append("', 'yyyy-MM-dd HH24:mi:ss')");
		}
		    <#elseif column.isStringColumn>
	    if (!StringUtils.isBlank(${classNameLower}QueryPageBO.get${column.columnName}())) {
			hqlCondition.append(" AND o.${column.columnNameLower} LIKE :${column.columnNameLower}");
			param.put("${column.columnNameLower}", "%"+${classNameLower}QueryPageBO.get${column.columnName}()+"%");
		}
	    	<#else>
	    if (${classNameLower}QueryPageBO.get${column.columnName}() != null) {
			hqlCondition.append(" AND o.${column.columnNameLower} = :${column.columnNameLower}");
			param.put("${column.columnNameLower}", ${classNameLower}QueryPageBO.get${column.columnName}());
		}
			</#if>
		</#list>
		hql.append(hqlCondition);
		hqlCount.append(hqlCondition);
		<#if (table.pkColumn)??>
		hql.append(" ORDER BY o.${table.pkColumn.columnNameLower} DESC");
		<#else>
		<#list table.columns as column>
	    	<#if table.pkCount==0 && column_index==0>
		hql.append(" ORDER BY o.${column.columnNameLower} DESC");
			</#if>
		</#list>
		</#if>
		List<BO${className}> bo${className}s = null;
		Page<${className}BO> page = new Page<${className}BO>();
		if ((${classNameLower}QueryPageBO.getPageNo() != null && ${classNameLower}QueryPageBO.getPageNo().compareTo(0) > 0) && (${classNameLower}QueryPageBO.getPageSize() != null && ${classNameLower}QueryPageBO.getPageSize().compareTo(0) > 0)) {
			Long count = (Long) commonDAO.findSingleResultByJPAQL(hqlCount.toString(), param);
			if (count == null || count.compareTo(0L) <= 0) {
				return page;
			}
			page.setRowCount(count.intValue());
			bo${className}s = commonDAO.findByJPAQL(hql.toString(), param, ${classNameLower}QueryPageBO.getPageNo(), ${classNameLower}QueryPageBO.getPageSize());
			page.generatePageCount(${classNameLower}QueryPageBO.getPageSize());
		} else {
			bo${className}s = commonDAO.findByJPAQL(hql.toString(), param);
		}
		page.setPageData(${className}Mapper.INSTANCE.domainToBo(bo${className}s));
		return page;
	}
	
	@Override
	public boolean verify(${className}BO bo, StringBuffer errorMsg) throws Exception {
		// TODO 此处添加校验
		if(StringUtils.isNotBlank(errorMsg.toString())){
			return true;
		}
		return false;
	}

	@Override
	public boolean importExcel(MultipartFile file, List<${className}DTO> successList,
			List<${className}DTO> errorList, StringBuffer errorMsg) {
		List<${className}Excel> excellist = null;
		try{
			excellist = GenerateExport.formulaImportExcel(file, 0, 1, ${className}Excel.class);
			List<${className}BO> boList = ${className}Mapper.INSTANCE.excelToBo(excellist);
			int i = 1;
 			for(${className}BO bo : boList){
				StringBuffer rowErrorMsg = new StringBuffer("");
				if (this.verify(bo, rowErrorMsg)) {
					// TODO 此处在BO中添加错误信息 ("第 " + i + " 行数据格式有误, " + rowErrorMsg.toString())
					errorList.add(${className}Mapper.INSTANCE.boToDto(bo));
				}
				i++;
			}
			if(!CollectionUtils.isEmpty(errorList)){
		        errorMsg.append("文档内容错误");
				return false;
			}
			for(${className}BO bo : boList){
				successList.add(${className}Mapper.INSTANCE.boToDto(this.save(bo)));
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg.append("执行文件导入出现未知错误请联系管理员");
			return false;
		}
		return true;
	}

	@Override
	public void downExcelByConds(${className}QueryPageBO ${classNameLower}QueryPageBO, HttpServletResponse response)
			throws Exception {
		Page<${className}BO> pages = this.findByConds(${classNameLower}QueryPageBO);
		List<${className}Excel> excellist = new ArrayList<${className}Excel>();
		for(${className}BO bo : pages.getPageData()){
			excellist.add(${className}Mapper.INSTANCE.boToExcel(bo));
		}
		GenerateExport.exportExcel(excellist, null, "${tableRemarks}", ${className}Excel.class, "${tableRemarks}", response);
	}

	@Override
	public boolean receiveExcel(File file, List<${className}BO> successList, List<${className}BO> errorList,
			StringBuffer errorMsg) throws Exception {
		String fileName = "";
		if (file.getName().indexOf(".xls") != -1) {
			fileName = file.getName().substring(0, file.getName().length() - 4);
		} else if (file.getName().indexOf(".xlsx") != -1) {
			fileName = file.getName().substring(0, file.getName().length() - 5);
		} else {
			throw new Exception("无法获取文件名称");
		}
		${className}QueryPageBO ${classNameLower}QueryPageBO = new ${className}QueryPageBO();
		${classNameLower}QueryPageBO.setYear(fileName);
		Page<${className}BO> page = this.findByConds(${classNameLower}QueryPageBO);
		if (!CollectionUtils.isEmpty(page.getPageData())) {
			for (${className}BO ${classNameLower}BO : page.getPageData()) {
				this.deleteByPrimaryKey(${classNameLower}BO.get<#list table.columns as column><#if column.pk>${column.columnName}()</#if></#list>);
			}
		}
		List<${className}Excel> excellist = null;
		try{
			excellist = GenerateExport.importExcel(file, 0, 1, ${className}Excel.class);
			List<${className}BO> boList = ${className}Mapper.INSTANCE.excelToBo(excellist);
			int i = 1;
			for (${className}BO bo : boList) {
				StringBuffer rowErrorMsg = new StringBuffer("");
				if (!this.verify(bo, rowErrorMsg)) {
					errorList.add(bo);
					errorMsg.append("第【" + i + "】行数据格式有误, " + rowErrorMsg.toString());
				}
				i++;
			}
			if (errorList.size() > 0) {
				return false;
			}
			for (${className}BO bo : boList) {
				if (StringUtils.isNotBlank(fileName)) {
					// TODO 此处配置导入批次  bo.setYear(fileName);
				} else {
					throw new Exception("导入批次不可为空");
				}
				successList.add(this.save(bo));
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg.append("执行文件导入出现未知错误请联系管理员, " + e.getMessage());
			return false;
		}
		return true;
	}
	
}
