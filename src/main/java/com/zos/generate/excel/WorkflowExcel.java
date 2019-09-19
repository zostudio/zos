package com.zos.generate.excel;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.extern.slf4j.Slf4j;

/**
 * 流程模版
 * 
 * @author joker
 */
@Slf4j
public class WorkflowExcel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8165176226095524532L;

	@Excel(name = "序号", orderNum = "0", width=6)
	private Long sequence;
	
	/**
	 * 模版项ID
	 */
	@Excel(name = "wf_item_id", orderNum = "1", width=10)
	private java.lang.Long wfItemId;
	
	/**
	 * 流程模版主键
	 */
	@Excel(name = "process_key", orderNum = "2", width=25)
	private java.lang.String processKey;
	
	/**
	 * 阶段编号
	 */
	@Excel(name = "phase_id", orderNum = "3", width=10)
	private java.lang.String phaseId;
	
	/**
	 * 阶段名称
	 */
	@Excel(name = "phase_name", orderNum = "4", width=25)
	private java.lang.String phaseName;
	
	/**
	 * 任务名称
	 */
	@Excel(name = "vm_task_name", orderNum = "5", width=25)
	private java.lang.String vmTaskName;
	
	/**
	 * 任务编号
	 */
	@Excel(name = "vm_task_no", orderNum = "6", width=25)
	private java.lang.String vmTaskNo;
	
	/**
	 * 环节ID
	 */
	@Excel(name = "link_id", orderNum = "7", width=8)
	private java.lang.Long linkId;
	
	/**
	 * 环节编号
	 */
	@Excel(name = "link_no", orderNum = "8", width=25)
	private java.lang.String linkNo;
	
	/**
	 * 环节类型
	 */
	@Excel(name = "link_no_type", orderNum = "9", width=12)
	private java.lang.String linkNoType;
	
	/**
	 * 权限ID
	 */
	@Excel(name = "role_code", orderNum = "10", width=10)
	private java.lang.String roleCode;
	
	/**
	 * 页面链接
	 */
	@Excel(name = "link_url", orderNum = "11", width=8)
	private java.lang.String linkUrl;
	
	/**
	 * 是否打单
	 */
	@Excel(name = "is_print", orderNum = "12", width=8)
	private java.lang.Boolean isPrint;
	
	/**
	 * 是否可以回退
	 */
	@Excel(name = "can_back", orderNum = "13", width=9)
	private java.lang.String canBack;
	
	/**
	 * 回退按钮
	 */
	@Excel(name = "back_btn", orderNum = "14", width=9)
	private java.lang.String backBtn;
	
	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

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
	
	public void logInsertSql() {
		log.info(this.assembleSql());
	}
	
	public String getInsertSql() {
		return this.assembleSql();
	}
	
	private String assembleSql() {
		StringBuffer sqlBuffer = new StringBuffer("\n");
		sqlBuffer.append("insert into ALM_WORKFLOW (WF_ITEM_ID, PROCESS_KEY, PHASE_ID, PHASE_NAME, VM_TASK_NAME, VM_TASK_NO, LINK_ID, LINK_NO, LINK_NO_TYPE, ROLE_CODE, LINK_URL, IS_PRINT, CAN_BACK, BACK_BTN)");
		sqlBuffer.append("\n");
		sqlBuffer.append("values (");
		sqlBuffer.append(StringUtils.isBlank(String.valueOf(this.getWfItemId())) ? "" : this.getWfItemId());
		sqlBuffer.append(", '");
		sqlBuffer.append(StringUtils.isBlank(this.getProcessKey()) ? "" : this.getProcessKey());
		sqlBuffer.append("', '', '', '");
		sqlBuffer.append(StringUtils.isBlank(this.getVmTaskName()) ? "" : this.getVmTaskName());
		sqlBuffer.append("', '");
		sqlBuffer.append(StringUtils.isBlank(this.getVmTaskNo()) ? "" : this.getVmTaskNo());
		sqlBuffer.append("', ");
		sqlBuffer.append(StringUtils.isBlank(String.valueOf(this.getLinkId())) ? "" : this.getLinkId());
		sqlBuffer.append(", '");
		sqlBuffer.append(StringUtils.isBlank(this.getLinkNo()) ? "" : this.getLinkNo());
		sqlBuffer.append("', '");
		sqlBuffer.append(StringUtils.isBlank(this.getLinkNoType()) ? "" : this.getLinkNoType());
		sqlBuffer.append("', '', '', null, '");
		sqlBuffer.append(StringUtils.isBlank(this.getCanBack()) ? "" : this.getCanBack());
		sqlBuffer.append("', '");
		sqlBuffer.append(StringUtils.isBlank(this.getBackBtn()) ? "" : this.getBackBtn());
		sqlBuffer.append("');");
		return sqlBuffer.toString();
	}
}

