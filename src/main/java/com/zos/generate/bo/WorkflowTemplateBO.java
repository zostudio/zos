package com.zos.generate.bo;

/**
 * 
 * 
 * @author joker
 */
public class WorkflowTemplateBO {
	
	/**
	 * 
	 */
	private java.lang.Long templateId;
	
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

