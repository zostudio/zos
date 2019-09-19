package com.zos.generate.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * 
 * 
 * @author joker
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "ALM_WORKFLOW_TEMPLATE")
public class BOWorkflowTemplate {
	
	// 
	@NotNull 
	private java.lang.Long templateId;
	// 
	@Length(max=4000)
	private java.lang.String processKey;
	// 
	@Length(max=10)
	private java.lang.String objType;
	// 
	@Length(max=200)
	private java.lang.String processName;

	public void setTemplateId(java.lang.Long value) {
		this.templateId = value;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TEMPLATE_ID", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
	public java.lang.Long getTemplateId() {
		return this.templateId;
	}	    
	
	@Column(name = "PROCESS_KEY", unique = false, nullable = true, insertable = true, updatable = true, length = 4000)
	public java.lang.String getProcessKey() {
		return this.processKey;
	}
	
	public void setProcessKey(java.lang.String value) {
		this.processKey = value;
	}
	
	@Column(name = "OBJ_TYPE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public java.lang.String getObjType() {
		return this.objType;
	}
	
	public void setObjType(java.lang.String value) {
		this.objType = value;
	}
	
	@Column(name = "PROCESS_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public java.lang.String getProcessName() {
		return this.processName;
	}
	
	public void setProcessName(java.lang.String value) {
		this.processName = value;
	}
}

