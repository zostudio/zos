package com.zos.generate.bo;

/**
 * 流程模版
 * 
 * @author joker
 */
public class WorkflowQueryPageBO {
	
	/**
	 * 模版项ID(开始)
	 */
	private java.lang.Long wfItemIdBegin;
	
	/**
	 * 模版项ID(结束)
	 */
	private java.lang.Long wfItemIdEnd;
	
	/**
	 * 流程模版主键
	 */
	private java.lang.String processKey;
	
	/**
	 * 阶段编号
	 */
	private java.lang.String phaseId;
	
	/**
	 * 阶段名称
	 */
	private java.lang.String phaseName;
	
	/**
	 * 任务名称
	 */
	private java.lang.String vmTaskName;
	
	/**
	 * 任务编号
	 */
	private java.lang.String vmTaskNo;
	
	/**
	 * 环节ID(开始)
	 */
	private java.lang.Long linkIdBegin;
	
	/**
	 * 环节ID(结束)
	 */
	private java.lang.Long linkIdEnd;
	
	/**
	 * 环节编号
	 */
	private java.lang.String linkNo;
	
	/**
	 * 环节类型
	 */
	private java.lang.String linkNoType;
	
	/**
	 * 权限ID
	 */
	private java.lang.String roleCode;
	
	/**
	 * 页面链接
	 */
	private java.lang.String linkUrl;
	
	/**
	 * 是否打单
	 */
	private java.lang.Boolean isPrint;
	
	/**
	 * 是否可以回退
	 */
	private java.lang.String canBack;
	
	/**
	 * 回退按钮
	 */
	private java.lang.String backBtn;
	
	/**
	 * 分页页码
	 */
	private Integer pageNo;
	
	/**
	 * 分页步长
	 */
	private Integer pageSize;
	public java.lang.Long getWfItemIdBegin() {
		return this.wfItemIdBegin;
	}
	
	public void setWfItemIdBegin(java.lang.Long value) {
		this.wfItemIdBegin = value;
	}
	
	public java.lang.Long getWfItemIdEnd() {
		return this.wfItemIdEnd;
	}
	
	public void setWfItemIdEnd(java.lang.Long value) {
		this.wfItemIdEnd = value;
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
	public java.lang.Long getLinkIdBegin() {
		return this.linkIdBegin;
	}
	
	public void setLinkIdBegin(java.lang.Long value) {
		this.linkIdBegin = value;
	}
	
	public java.lang.Long getLinkIdEnd() {
		return this.linkIdEnd;
	}
	
	public void setLinkIdEnd(java.lang.Long value) {
		this.linkIdEnd = value;
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
	
	public Integer getPageNo() {
		return this.pageNo;
	}
	
	public Integer getPageSize() {
		return this.pageSize;
	}
	
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}

