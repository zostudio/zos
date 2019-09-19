package com.zos.generate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 
 * @author joker
 */
@ApiModel(description = "")
public class WorkflowTemplateDTO {
	
	@ApiModelProperty(value = "模板主键")
	private java.lang.Long templateId;
	
	@ApiModelProperty(value = "流程主键")
	private java.lang.String processKey;
	
	@ApiModelProperty(value = "流程类型")
	private java.lang.String objType;
	
	@ApiModelProperty(value = "流程名称")
	private java.lang.String processName;
	
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
}

