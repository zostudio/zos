package com.zos.generate.excel;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author joker
 */
@Slf4j
public class WorkflowTemplateExcel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8822653309242248923L;
	
	@Excel(name = "序号", orderNum = "0", width=6)
	private Long sequence;

	/**
	 * 
	 */
	@Excel(name = "template_id", orderNum = "1", width=14)
	private java.lang.Long templateId;
	
	/**
	 * 
	 */
	@Excel(name = "process_key", orderNum = "2", width=25)
	private java.lang.String processKey;
	
	/**
	 * 
	 */
	@Excel(name = "obj_type", orderNum = "3", width=25)
	private java.lang.String objType;
	
	/**
	 * 
	 */
	@Excel(name = "process_name", orderNum = "3", width=88)
	private java.lang.String processName;
	
	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setTemplateId(java.lang.Long value) {
		this.templateId = value;
	}
	
	public java.lang.Long getTemplateId() {
		return this.templateId;
	}	    
	
	public java.lang.String getProcessKey() {
		return this.processKey;
	}
	
	public void setProcessKey(java.lang.String value) {
		this.processKey = value;
	}
	
	public java.lang.String getObjType() {
		return this.objType;
	}
	
	public void setObjType(java.lang.String value) {
		this.objType = value;
	}
	
	public java.lang.String getProcessName() {
		return this.processName;
	}
	
	public void setProcessName(java.lang.String value) {
		this.processName = value;
	}
	
	public void logInsertSql() {
		StringBuffer inserSql = new StringBuffer("\n");
		inserSql.append("insert into Alm_Workflow_Template (TEMPLATE_ID, PROCESS_KEY, OBJ_TYPE, PROCESS_NAME)");
		inserSql.append("\n");
		inserSql.append("values (null, '");
		inserSql.append(StringUtils.isBlank(this.getProcessKey()) ? "" : this.getProcessKey());
		inserSql.append("', '', '");
		inserSql.append(StringUtils.isBlank(this.getProcessName()) ? "" : this.getProcessName());
		inserSql.append("');");
		log.info(inserSql.toString());
	}
}

