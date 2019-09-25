package com.zos.generate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 暂无表注释
 * 
 * @author 01Studio
 */
@ApiModel(description = "暂无表注释")
public class WorkflowTemplateDTO {
	
	@ApiModelProperty(value = "流程模版主键", example = "0")
	private java.lang.Long templateId;
	
	@ApiModelProperty(value = "流程唯一标识", example = "String")
	private java.lang.String processKey;
	
	@ApiModelProperty(value = "流程唯一代码", example = "String")
	private java.lang.String objType;
	
	@ApiModelProperty(value = "流程唯一名称", example = "String")
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

