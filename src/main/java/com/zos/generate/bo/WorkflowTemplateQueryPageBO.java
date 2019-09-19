package com.zos.generate.bo;

/**
 * 
 * 
 * @author joker
 */
public class WorkflowTemplateQueryPageBO {
	
	/**
	 * (开始)
	 */
	private java.lang.Long templateIdBegin;
	
	/**
	 * (结束)
	 */
	private java.lang.Long templateIdEnd;
	
	/**
	 * 
	 */
	private java.lang.String processKey;
	
	/**
	 * 
	 */
	private java.lang.String objType;
	
	/**
	 * 
	 */
	private java.lang.String processName;
	
	/**
	 * 分页页码
	 */
	private Integer pageNo;
	
	/**
	 * 分页步长
	 */
	private Integer pageSize;
	public java.lang.Long getTemplateIdBegin() {
		return this.templateIdBegin;
	}
	
	public void setTemplateIdBegin(java.lang.Long value) {
		this.templateIdBegin = value;
	}
	
	public java.lang.Long getTemplateIdEnd() {
		return this.templateIdEnd;
	}
	
	public void setTemplateIdEnd(java.lang.Long value) {
		this.templateIdEnd = value;
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

