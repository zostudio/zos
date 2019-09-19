package com.zos.generate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程模版
 * 
 * @author joker
 */
@ApiModel(description = "流程模版")
public class WorkflowDTO {
	
	@ApiModelProperty(value = "模版项ID")
	private java.lang.Long wfItemId;
	
	@ApiModelProperty(value = "流程模版主键")
	private java.lang.String processKey;
	
	@ApiModelProperty(value = "阶段编号")
	private java.lang.String phaseId;
	
	@ApiModelProperty(value = "阶段名称")
	private java.lang.String phaseName;
	
	@ApiModelProperty(value = "任务名称")
	private java.lang.String vmTaskName;
	
	@ApiModelProperty(value = "任务编号")
	private java.lang.String vmTaskNo;
	
	@ApiModelProperty(value = "环节ID")
	private java.lang.Long linkId;
	
	@ApiModelProperty(value = "环节编号")
	private java.lang.String linkNo;
	
	@ApiModelProperty(value = "环节类型")
	private java.lang.String linkNoType;
	
	@ApiModelProperty(value = "权限ID")
	private java.lang.String roleCode;
	
	@ApiModelProperty(value = "页面链接")
	private java.lang.String linkUrl;
	
	@ApiModelProperty(value = "是否打单")
	private java.lang.Boolean isPrint;
	
	@ApiModelProperty(value = "是否可以回退")
	private java.lang.String canBack;
	
	@ApiModelProperty(value = "回退按钮")
	private java.lang.String backBtn;
	
	public void setWfItemId(java.lang.Long value) {
		this.wfItemId = value;
	}
	
	public java.lang.Long getWfItemId() {
		return this.wfItemId;
	}	    
	
	public java.lang.String getProcessKey() {
		return this.processKey;
	}
	
	public void setProcessKey(java.lang.String value) {
		this.processKey = value;
	}
	
	public java.lang.String getPhaseId() {
		return this.phaseId;
	}
	
	public void setPhaseId(java.lang.String value) {
		this.phaseId = value;
	}
	
	public java.lang.String getPhaseName() {
		return this.phaseName;
	}
	
	public void setPhaseName(java.lang.String value) {
		this.phaseName = value;
	}
	
	public java.lang.String getVmTaskName() {
		return this.vmTaskName;
	}
	
	public void setVmTaskName(java.lang.String value) {
		this.vmTaskName = value;
	}
	
	public java.lang.String getVmTaskNo() {
		return this.vmTaskNo;
	}
	
	public void setVmTaskNo(java.lang.String value) {
		this.vmTaskNo = value;
	}
	
	public java.lang.Long getLinkId() {
		return this.linkId;
	}
	
	public void setLinkId(java.lang.Long value) {
		this.linkId = value;
	}
	
	public java.lang.String getLinkNo() {
		return this.linkNo;
	}
	
	public void setLinkNo(java.lang.String value) {
		this.linkNo = value;
	}
	
	public java.lang.String getLinkNoType() {
		return this.linkNoType;
	}
	
	public void setLinkNoType(java.lang.String value) {
		this.linkNoType = value;
	}
	
	public java.lang.String getRoleCode() {
		return this.roleCode;
	}
	
	public void setRoleCode(java.lang.String value) {
		this.roleCode = value;
	}
	
	public java.lang.String getLinkUrl() {
		return this.linkUrl;
	}
	
	public void setLinkUrl(java.lang.String value) {
		this.linkUrl = value;
	}
	
	public java.lang.Boolean getIsPrint() {
		return this.isPrint;
	}
	
	public void setIsPrint(java.lang.Boolean value) {
		this.isPrint = value;
	}
	
	public java.lang.String getCanBack() {
		return this.canBack;
	}
	
	public void setCanBack(java.lang.String value) {
		this.canBack = value;
	}
	
	public java.lang.String getBackBtn() {
		return this.backBtn;
	}
	
	public void setBackBtn(java.lang.String value) {
		this.backBtn = value;
	}
}

